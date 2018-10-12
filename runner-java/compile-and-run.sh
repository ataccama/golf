#!/bin/bash

set -e

mv code code.java
javac code.java
rm code.java

for classfile in *.class; do
    classname=${classfile%.*}
    #echo $classname

    #Execute grep with -q option to not display anything on stdout when the match is found
	if javap -public "$classname" | grep -q -E 'public static void main\(java.lang.String(\[\]|\.{3})\)'; then
        java $classname | tee stdout
        exit 0;
    fi
done

>&2 echo "No main method was found."
exit 2
