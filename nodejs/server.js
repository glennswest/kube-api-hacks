'use strict';
const port = 8080
const express = require('express')
const path = require('path')
const app = express()
const api = require('kubernetes-client');
const core = new api.Core({
  url: 'https://192.168.137.2:8443',
  insecureSkipTlsVerify: true,
  namespace: 'default',
  auth: {
    bearer: 'eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJkZWZhdWx0Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZWNyZXQubmFtZSI6InN1cGVyLWRlbW8tdXNlci10b2tlbi1jOWYxdCIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VydmljZS1hY2NvdW50Lm5hbWUiOiJzdXBlci1kZW1vLXVzZXIiLCJrdWJlcm5ldGVzLmlvL3NlcnZpY2VhY2NvdW50L3NlcnZpY2UtYWNjb3VudC51aWQiOiI5MjVjOGVkMi02Y2ZmLTExZTctYmQyMS01MjU0MDBiMzdkMWEiLCJzdWIiOiJzeXN0ZW06c2VydmljZWFjY291bnQ6ZGVmYXVsdDpzdXBlci1kZW1vLXVzZXIifQ.GCUB3KPmTSVUVKBMJPKA-zsz-Bnpq4-LgW5lzaSyb-shB-OZUa0QlBOgCd8BQrpP_Cp5VT52hYKIxsAf6agWffsKClowACQ_0Y_Qoe1fAexVh8jRRjPPz5jJPYZrJ4Gpr-7vVMkHAhw0ZFZShmWA0kvORIwBSCwE-ZROlaXDqQb86xgKa8OOaZBo2TeXfsMlHd_eQuFu4WgB6EVNFOj99x9QmTUAc-pgjepUMdAcLVUYe21o2XankXNxb3ZIPUzhfaQ1iY3_WGqZ8-C8Q2ds5Xq9g8t9Ejt9DsnXKwpiNfvOTM1lgs80lxUgiPVsCAYuiAPgO_GdNazTR3TYOfA-Yw'
  }
});
function print(err, result) {
  console.log(JSON.stringify(err || result, null, 2));
}
app.get('*', (req, res) => {
    //core.ns.pods.get(print);
    core.ns.pods.get(function(err, result) {
      var all = result.items;
      var pods = [];
      for (var i = 0, len = all.length; i < len; i++) {
        pods.push(all[i].metadata.name);
      }
      res
        .status(200)
        .json(pods)
    })
});
app.listen(port)
