# kube-api-hacks

uses https://github.com/godaddy/kubernetes-client

```
cd kube-api-hacks/nodejs
```

## locally
```
npm i kubernetes-client express
npm init -f
npm start
```
## openshift
```
oc new-project api
oc new-build --binary --name=api -l app=api -i nodejs
oc start-build api --from-dir=. --follow
oc new-app api
oc expose svc api
```

## openshift auth

TODO: give the service account as many permissions as you need. mount as a secret.

```
oc create serviceaccount 'super-demo-user' -n 'default'
oc adm policy add-cluster-role-to-user cluster-admin system:serviceaccount:default:super-demo-user
oc get -n default sa/super-demo-user --template='{{range .secrets}}{{printf "%s\n" .name}}{{end}}'
oc get -n default secrets super-demo-user-token-c9f1t --template='{{.data.token}}' | base64 -d
```