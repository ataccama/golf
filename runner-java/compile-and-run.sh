#!/bin/bash

f=code.java
mv code "$f"
out=`javac "$f" 2>&1`
ex=$?

if [ $ex -gt 0 ]; then
	if grep -q "is public, should be declared in a file named" <<<$out; then
		set -e
		f=`sed -n '/is public, should be declared in a file named/ { s/.*file named \(.*\)/\1/; p; q}' <<<$out`
		mv code.java "$f"
		javac "$f"
	else
		>&2 echo "$out"
		exit $ex
	fi
else
	f=code.java
fi
rm "$f"

set -e

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
