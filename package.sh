#!/bin/bash

package() {
	echo "Packaging $1"
	(
		cd "$1"

		if [ -f build.gradle ]; then
			gradle clean build
		fi

		DIR=$(basename "`pwd`")
		TAG="juriad/$DIR"

		docker build -t "$TAG" .
		docker push "$TAG"
	)
}

for dir in "$@"; do
	package "$dir"
done
