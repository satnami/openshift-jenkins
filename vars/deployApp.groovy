#!/usr/bin/groovy

def call(project, app, tag){
    sh "oc new-app --name=${app} -n ${project} -l app=${app} --image-stream=${project}/${app}:${tag} || echo 'Aplication already Exists'"
    sh "oc expose service ${app} -n ${project} || echo 'Service already exposed'"
    sh "oc patch dc/${app} -n ${project} -p '{\"spec\":{\"template\":{\"spec\":{\"containers\":[{\"name\":\"${app}\"}]}}}}'"
}