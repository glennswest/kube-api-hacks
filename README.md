# kube-api-hacks

## nodejs version

https://github.com/eformat/kube-api-hacks/tree/master/nodejs

## use a long lived service account token

oc create serviceaccount 'super-demo-user' -n 'default' 

oc adm policy add-cluster-role-to-user cluster-admin 

system:serviceaccount:default:super-demo-user 

oc get -n default sa/super-demo-user --template='{{range.secrets}}{{printf "%s\n" .name}}{{end}}' 

oc get -n default secrets super-demo-user-token-c9f1t --template='{{.data.token}}' | base64 -d 

give this service account as many permissions as you need :)


