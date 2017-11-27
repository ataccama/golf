#!/bin/bash

set -e

FILE=`mktemp`
mv code "$FILE"

node - <"$FILE" | tee stdout

rm "$FILE"
