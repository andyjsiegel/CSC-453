#!/bin/bash

zipfile=$1

FILES=( problem1.json problem2.json problem3.json problem4.json problem5.gv problem6.gv  )
HASHES=(  1dc8f3ddc770aa55f3520fbd945d9d54153a0f51f737469948c1b345def0f217 c98ca963a4cb421eb3c543801aca2182ed226f4b98834d6adcc6ee3e4b9f0ee1 1d36afc183c334cc53cc2e1dd6d2dfd313085d973ef6a888d79de3887053090e efd37989504c2eb09079b80a547c3f23e668e7bdb3fc87e289a3130b55099b75 e54a18f584ebe362d8a2edd137ff0fd31fda222bc13e2f5b5d7d8fb2fb9349b2 75fd085a9f126014655e06d9e9c1b96c8256c2fa60cc19586347b1957f6e1c66  )
echo "==== This is the LigerLabs assignment validator for CSC 453, Assignment 2, Spring 2026 ===="

##############################################################################################
# Check that programs exist
##############################################################################################

echo "==== 1: Checking that necessary programs have been installed."

if ! command -v jq &> /dev/null
then
   echo "Before running the validation, please install 'jq'."
   echo "On Linux:"
   echo "   > sudo apt-get install jq"
   echo "On MacOS:"
   echo "   > sudo port install jq"
   echo "   > brew install jq"
   exit -1
fi

if ! command -v shasum &> /dev/null
then
   echo "Before running the validation, please install 'shasum'."
   echo "On Linux:"
   echo "   > sudo apt-get install shasum"
   echo "On MacOS:"
   echo "   > sudo port install shasum"
   echo "   > brew install shasum"
   exit -1
fi

if ! command -v pngcheck &> /dev/null
then
   echo "Before running the validation, please install 'pngcheck'."
   echo "On Linux:"
   echo "   > sudo apt-get install pngcheck"
   echo "On MacOS:"
   echo "   > sudo port install pngcheck"
   echo "   > brew install pngcheck"
   exit -1
fi

if ! command -v dot &> /dev/null
then
   echo "Before running the validation, please install 'dot' (graphviz)."
   echo "On Linux:"
   echo "   > sudo apt-get install graphviz"
   echo "On MacOS:"
   echo "   > sudo port install graphviz"
   echo "   > brew install graphviz"
   exit -1
fi

if ! command -v gcc &> /dev/null
then
   echo "Before running the validation, please install 'gcc'."
   exit -1
fi

##############################################################################################
# Check input name
##############################################################################################

echo "==== 2: Checking that the zip file has a valid name."

if [ "$zipfile" == "" ] || [ "$zipfile" == "firstname_lastname.zip" ] || [ "$zipfile" == "pat_jones.zip" ]
then
   echo "Call validate.sh like this (replacing pat_jones with your own name):"
   echo "   > ./validate.sh pat_jones.zip"
   exit -1
fi

if [[ "$zipfile" == "*.zip" ]]
then
   echo "ERROR: The zip doesn't have the '.zip' extension"
   exit -1
fi


##############################################################################################
# Check that this is a zip file
##############################################################################################

echo "==== 3: Checking that this is a valid zip file."

/bin/rm -rf VALIDATE
/bin/mkdir VALIDATE

/bin/cp $zipfile VALIDATE
cd VALIDATE

unzip -qq -t $zipfile
if [ "$?" != 0 ] 
then
   echo "ERROR: $zipfile is not a valid zip file"
   exit -1
fi

##############################################################################################
# Check the directory structure
##############################################################################################

echo "==== 4: Checking that the zip file has a valid directory structure."

unzip -qq $zipfile

name=`basename $zipfile .zip`

if ! [ -d $name ]
then
   echo "ERROR: $zipfile has the wrong directory structure. It should consist of a single directory named '$name' in which your answer files reside."
   exit -1
fi

##############################################################################################
# Check the that all problem files exist
##############################################################################################

echo "==== 5: Checking that all problem files exist inside the zip file."

cd $name

for i in ${FILES[*]}
do
   if ! [[ -e  $i ]]
   then
      echo "ERROR: Problem file '$i' does not exist."
      exit -1
   fi
done   

##############################################################################################
# Check that all problems have been answered
##############################################################################################

echo "==== 6: Checking that all problems have been answered."

for i in "${!FILES[@]}"
do
   found=`shasum -a 256 "${FILES[$i]}"`
   expected=${HASHES[$i]}
   if [[ "$sha" == "$expected" ]]
     then
        echo "WARNING: Answer file '${FILES[$i]}' is the same as the template you were given - did you forget to solve this problem?"
     fi
done   

##############################################################################################
# Check that the answer files have valid structure
##############################################################################################

echo "==== 7: Checking that all answer files have valid structure."

for i in ${FILES[*]}
do
   case $i in
      *.json)
         if ! cat "$i" | jq > /dev/null 2>&1
         then
           echo "ERROR: There is a problem with json file '$i':"
           cat "$i" | jq 
         fi
         ;;
      *.png)
         if ! pngcheck "$i" > /dev/null 2>&1
         then
           echo "ERROR: There is a problem with png file '$i':"
           pngcheck $i
         fi
         ;;
      *.gv)
         if ! dot "$i" > /dev/null 2>&1
         then
           echo "ERROR: There is a problem with Graphviz file '$i':"
           dot $i
         fi
         ;;
      *.c)
         if ! gcc -fsyntax-only "$i" > /dev/null 2>&1
         then
           echo "ERROR: There is a problem with the C source file '$i':"
           gcc $i
         fi
         ;;
   esac
done   

