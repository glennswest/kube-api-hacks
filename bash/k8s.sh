#!/bin/bash

OAUTH_TOKEN=`oc whoami -t`
MASTER_HOST=192.168.137.2
PROJECT_NAME=default

curl -s -k -H "Authorization: Bearer $OAUTH_TOKEN" https://$MASTER_HOST:8443/api/v1/namespaces/$PROJECT_NAME/pods/ | jq '.items[] .metadata.labels'
