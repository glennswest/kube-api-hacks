# kube-api-hacks
cd kube-api-hacks/nodejs

## locally
npm i kubernetes-client express
npm init -f
npm start

## openshift
oc new-project api
oc new-build --binary --name=api -l app=api -i nodejs
oc start-build api --from-dir=. --follow
oc new-app api
