job [[ .id ]] {
  region = "prg-krl"
  datacenters = [ "[[ .dc ]]" ]
  type = "service"
  meta {
    "Deployed by" = "[[ .author ]]"
    "URL" = "https://[[ .id ]].[[ .dc ]].atc"
  }

  group "dep" {
    count = 1
    
    constraint {
      attribute = "${attr.unique.hostname}"
      value     = "n02"
    }
    
    task "rabbitmq" {
      driver = "docker"
      config {
        image = "juriad/rabbitmq:1.0"
        mounts = [
          {
            target = "/var/lib/rabbitmq"
            source = "[[ .id ]]-rabbitdata"

            volume_options {
              no_copy = false
              driver_config {
                name = "local"
              }
            }
          }
        ]
      }

      env {
        RABBITMQ_DEFAULT_USER = "rabbituser"
        RABBITMQ_DEFAULT_PASS = "rabbitpw"
      }

      service {
        port = "rmq"
        check {
          type = "tcp"
          interval = "10s"
          timeout = "2s"
        }
      }

      resources {
        cpu = 500
        memory = 1000
        network {
          mbits = 100
          port "rmq" {
            static = "5672"
          }

          port "rmq_admin" {
            static = "15672"
          }
        }
      }
    }
    
    task "postgres" {
      driver = "docker"
      config {
        image = "postgres:10"
        mounts = [
          {
            target = "/var/lib/postgresql/data/pgdata"
            source = "[[ .id ]]-pgdata"

            volume_options {
              no_copy = false
              driver_config {
                name = "local"
              }
            }
          }
        ]
      }

      env {
        PGDATA = "/var/lib/postgresql/data/pgdata"
        POSTGRES_DB = "golf"
        POSTGRES_USER = "root"
        POSTGRES_PASSWORD = "password123"
      }

      service {
        port = "pg"
        check {
          type = "tcp"
          interval = "10s"
          timeout = "2s"
        }
      }

      resources {
        cpu = 500
        memory = 1000
        network {
          mbits = 100
          port "pg" {
            static = "5432"
          }
        }
      }
    }
  }
  
  group "fe" {
    count = 1

    task "frontend" {
      driver = "docker"
      config {
        image = "juriad/frontend:1.1"
        port_map = {
          http = 80
        }
      }

      env {
        BACKEND = "http://[[ .id ]]-fe-gateway.service:18080"
      }

      service {
        port = "http"
        tags = [
          "lb.frontend.rule=Host:task.ataccama.com",
          "lb.tags=public",
          "lb.tags=http"
        ]
        check {
          type = "tcp"
          interval = "10s"
          timeout = "2s"
        }
      }

      resources {
        cpu = 500
        memory = 1000
        network {
          mbits = 100
          port "http" {
            static = "80"
          }
        }
      }
    }
    
    task "gateway" {
      driver = "docker"
      config {
        image = "juriad/gateway:1.0"
        port_map {
          http = 8080
        }
      }

      env {
        JDBC_CONNECTION = "jdbc:postgresql://[[ .id ]]-dep-postgres.service:5432/golf?currentSchema=public"
        JDBC_USERNAME = "root"
        JDBC_PASSWORD = "password123"
        RABBIT_HOST = "[[ .id ]]-dep-rabbitmq.service"
        RABBIT_USERNAME = "rabbituser"
        RABBIT_PASSWORD = "rabbitpw"
      }

      service {
        port = "http"
        check {
          type = "tcp"
          interval = "10s"
          timeout = "2s"
        }
      }

      resources {
        cpu = 500
        memory = 1000
        network {
          mbits = 100
          port "http" {
            static = "18080"
          }
        }
      }
    }
  }
  
  group "be" {
    count = 1
    
    constraint {
      attribute = "${attr.unique.hostname}"
      value     = "n06"
    }
    
    task "grader-fib" {
      driver = "docker"
      config {
        image = "juriad/grader-fib:1.0"
      }

      env {
        RABBIT_HOST = "[[ .id ]]-dep-rabbitmq.service"
        RABBIT_USERNAME = "rabbituser"
        RABBIT_PASSWORD = "rabbitpw"
      }
      
      service {
      }

      resources {
        cpu = 500
        memory = 1000
        network {
          mbits = 100
        }
      }
    }
    
    task "processor-java" {
      driver = "docker"
      config {
        image = "juriad/processor-java:1.1"
        volumes = [
          "/var/run/docker.sock:/var/run/docker.sock"
        ]
        mounts = [
          {
            target = "/solutions"
            source = "[[ .id ]]-solutions"

            volume_options {
              no_copy = false
              driver_config {
                name = "local"
              }
            }
          },
        ]
      }

      env {
        DOCKER_IMAGE = "juriad/runner-java:1.0"
        DOCKER_TIMEOUT = "5000"
        DOCKER_HOST = "unix:///var/run/docker.sock"
        RABBIT_HOST = "[[ .id ]]-dep-rabbitmq.service"
        RABBIT_USERNAME = "rabbituser"
        RABBIT_PASSWORD = "rabbitpw"
      }
      
      service {
      }

      resources {
        cpu = 500
        memory = 1000
        network {
          mbits = 100
        }
      }
    }
  }
}
