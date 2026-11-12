#!/bin/sh
set -e
if [ -z "$1" ]; then
  echo "Usage: ./commit_task.sh 1"
  exit 1
fi
git add "Task$(printf '%02d' "$1")"
git commit -m "Complete Assignment 4 Task $1"
