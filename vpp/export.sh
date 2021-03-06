#!/bin/bash
#Dit script wordt uitgevoerd voor elke commit (pre-commit hook), zodat er altijd afbeeldingen zijn van het visual paradigm project.

exit #Visual paradigm veranderd nogsteeds bestanden die niet veranderd zijn.



cd "$(dirname $0)"
vppFile="ipass.vpp"
vppFilePath="$PWD/$vppFile"
vppLockPath="$PWD/.$vppFile.lck"
if ! git diff --exit-code "$vppFilePath"; then
	rm images/*
	rm $vppLockPath
	~/Visual_Paradigm_16.0/scripts/ExportDiagramImage.sh -project "$vppFilePath" -out "$PWD/images" -diagram '*'  -type svg
	~/Visual_Paradigm_16.0/scripts/GenerateORM.sh -project "$vppFilePath" -out "$PWD/sql" -db 3 -ddl -dbmode DropAndCreate
	#for file in images/*.svg
	#do
	#	scour --create-groups --shorten-ids --enable-id-stripping --enable-comment-stripping --remove-metadata --strip-xml-prolog -p 3 "$file" | tee "$file.new" >/dev/null
	#	mv "$file.new" "$file"
	#	
	#done
	git add "$vppFilePath" images/* sql/*
	echo "Run again, VPP changed."
	exit 1
fi