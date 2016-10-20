!# bin/bash
# INITIALISATION

# git checkout --orphan gh-pages

# Update .gitignore
# Generate doc

#git push origin gh-pages


# DEPLOIEMENT
# Deploiement code here...
git checkout gh-pages
javadoc -protected -splitindex -use -author -version @/tmp/javadocargs.txt -classpath /home/jessy/.m2/repository/org/json/json/20160810/json-20160810.jar -d /home/jessy/Documents/blu/blu-desktop/docs
git add --all
git commit -m'Update doc version'
git push origin gh-pages
git checkout stats-section
