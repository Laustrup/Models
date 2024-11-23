#!/bin/bash

branch=$(git rev-parse --abbrev-ref HEAD)
usage="
This script guides the user through each step of deploying its project to maven, at this moment to a mvn github package.
The first step is simply to push to git, it will ensure that the master branch is up to date with the package, this step can be skipped though.
It needs to be in master branch, if it is not in master branch, it will not execute and display the user about the scenario.
"

for argument in "$@"
do
  if [ "$argument" = "help" ] || [ "$argument" = "-help" ] || [ "$argument" = "-h" ]
  then
    echo "$usage"
    exit 1
  fi
  if [[ "$argument" != *"-"* ]]
  then
    echo "Illegal action for argument $argument"
    exit 2
  fi
done

if [ "$branch" = "master" ]
then
  echo "
This program will give some prompts to deploy to maven and then execute the deploy.
Remember to have settings.xml updated in the .m2 directory.
It is as any other terminal program possible to exit with ctrl+c.
"

  echo "-- Choose git action"
  gitStatus="There were no push to the repository"
  gitOptions=("Push" "Push and commit" "Don't push")
  select option in "${gitOptions[@]}"
  do
    case $option in
      "Push")
        echo "Sure that you want to push to the repository without commits?
It will make sense if it has only just been merged from another branch.
Type y or yes"
        read -r decision
        if [ "$decision" = "y" ] || [ "$decision" = "yes" ]
        then
          echo "Will now push to repository without making a commit."
          ./../../shell-scripts/git-push.sh || exit 2
          gitStatus="Pushed without creating a commit"
          break
        fi
        ;;
      "Push and commit")
        echo "Type a description to your commit, when it has been typed, it will add files that are not mentioned in gitignore to git, create the commit with the description and push to repository.
Type nothing instead to push without creating a commit."
        read -r message
        if [ "$message" != "" ]
        then
          echo "Sure that you want to push with a commit that has the message "$message"? Files that are not mentioned in gitignore will also be added to git.
Type y or yes to push"
          read -r decision
          if [ "$decision" = "y" ] || [ "$decision" = "yes" ]
          then
            ./../../shell-scripts/git-push.sh -a -m "$message" || exit 2
            gitStatus="There was a push to the repository with the message " "$message"
            break
          fi
        else
          echo "Want to push without creating a new commit?"
          read -r decision
          if [ "$decision" = "y" ] || [ "$decision" = "yes" ]
          then
            echo "Will now push to repository without making a commit."
            ./../../shell-scripts/git-push.sh || exit 2
            gitStatus="Pushed without creating a commit"
            break
          fi
        fi
        ;;
      "Don't push")
        echo "You choose not to push anything"
        gitStatus="Nothing was pushed to repository"
        break
        ;;
    esac
  done

  echo "
-- Do you wish to skip tests? Type y or yes"
  read -r skipTests

  dSkipTests=""
  skipTestStatus="Tests will be run with deployment"
  if [ "$skipTests" = "y" ] || [ "$skipTests" = "yes" ]
  then
    echo "You choose to ignore tests"
    dSkipTests="-DskipTests"
    skipTestStatus="Tests will not be run with deployment"
  else
    echo "Tests will not be ignored"
  fi

  printf "
The chosen options were as following:

  * %s
  * %s

Do you wish to deploy? Type y or yes
" "$skipTestStatus" "$gitStatus"

  read -r doDeploy

  if [ "$doDeploy" = "y" ] || [ "$doDeploy" = "yes" ]
  then
    echo "Deploying"
    mvn deploy "$dSkipTests"
  else
    echo "Deployment cancelled"
  fi
else
  echo "You are not on the master branch, in order to deploy, you need to be on the master branch!"
fi
