#!/usr/bin/groovy

def call(project, app, version){
    sh "oc new-app -n ${project} --name ${app}-${version} ${app}:${version} -l app=${app},deploymentconfig=${app}"
    sh "oc patch svc/${app} -n ${project} -p '{\"spec\":{\"selector\":{\"app\": \"${app}\", \"deploymentconfig\": null}, \"sessionAffinity\":\"None\"}}'"
}