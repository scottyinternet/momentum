#!/usr/bin/env bash

#
# Convenience script for getting temporary AWS credentials from the command line
# and setting them to be the 'default' profile.
# https://aws.amazon.com/premiumsupport/knowledge-center/sso-temporary-credentials/
#
# IMPORTANT NOTES:
# - This assumes that the first profile in your AWS credentials file is the
# 'default' one, and will make updates to it accordingly. Specifically, it will
# overwrite lines 2 (key_id), 3 (secret), and 4 (token).
# - If you have multiple profiles in the file, make sure they don't start
# until line 5.
# - If the credentials file exists, it will make a single backup copy. If
# the credentials file does NOT exist, it will create an empty one.
#

role="$1"
if [ -z "$role" ]; then
  echo "usage: $(basename $0) ROLE"
  exit 1
fi

ssh_cache_file=$(find ~/.aws/sso/cache -maxdepth 1 -type f -name "[0-9]*.json")
if [ -z "$ssh_cache_file" ]; then
  echo "ERROR: Cannot find SSO file - have you run 'aws sso login --profile $role' first?"
  exit 1
fi

CREDS=~/.aws/credentials
if [ ! -e $CREDS ]; then
  echo "## Creating an empty credentials file"
  mkdir -p $(dirname $CREDS)
  touch $CREDS
  chmod 600 $CREDS
  echo "[default]" > $CREDS
  echo "aws_access_key_id=tmp" >> $CREDS
  echo "aws_secret_access_key=tmp" >> $CREDS
  echo "aws_session_token=tmp" >> $CREDS
else
  echo "## Making backup copy of existing credentials file"
  cp $CREDS ${CREDS}.bak
fi

echo "## Getting Account and Role Name from config"
CFG=~/.aws/config
account_id=$(grep "profile $role" -A6 $CFG | grep sso_account_id | awk '{print $3}')
role_name=$(grep "profile $role" -A6 $CFG | grep sso_role_name | awk '{print $3}')
token=$(cat $ssh_cache_file | jq -r '.accessToken')

# if credentials have expired (e.g. overnight) this commmand will fail, thus the check here
echo "## Getting SSO role credentials"
credentials=$(aws sso get-role-credentials --role-name $role_name --account-id $account_id --access-token $token)
if [ -z "$credentials" ]; then
  echo
  echo "ERROR: Have you run 'aws sso login --profile $role' first?"
  exit 1
fi

# at this point we should have everything we need and can get the variables
access_key=$(echo $credentials | jq -r '.roleCredentials.accessKeyId')
access_secret=$(echo $credentials | jq -r '.roleCredentials.secretAccessKey')
session_token=$(echo $credentials | jq -r '.roleCredentials.sessionToken')

# awk magic to indiscriminately replace lines 2-4 in the credentials file
cat $CREDS | \
  awk -v access_key=$access_key '{ if(NR==2) print "aws_access_key_id=" access_key; else print $0}' | \
  awk -v access_secret=$access_secret '{ if(NR==3) print "aws_secret_access_key=" access_secret; else print $0}' | \
  awk -v session_token=$session_token '{if(NR==4) print "aws_session_token=" session_token; else print $0}' > ${CREDS}.new
mv ${CREDS}.new $CREDS

echo "## Validating AWS Identity with the following:"
echo "Account ID : $account_id"
echo "Role       : $role"
echo "SSO Role   : $role_name"
echo
aws --no-cli-pager sts get-caller-identity
