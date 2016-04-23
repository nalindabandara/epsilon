#! /bin/bash
file="$1"
wc -w $file | cut -d' ' -f1
