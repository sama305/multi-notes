#!/bin/bash

pattern="$1"
replace="$2"

files=($pattern*)

for f in "${files[@]}"; do
	newname="$(echo "$f" | sed "s/$pattern/$replace/g")"
	printf "Rename $f to $newname? (Y/n)\n> "
	read inp
	if [ "$inp" = "y" ] || [ "$inp" = "Y" ]; then
		mv "$f" "$newname"
		echo "Operation successful"
	fi
done

