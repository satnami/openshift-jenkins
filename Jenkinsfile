podTemplate(
    inheritFrom: "maven", 
    label: "myJenkins", 
    cloud: "openshift", 
    volumes: [
        persistentVolumeClaim(claimName: "m2repo", mountPath: "/home/jenkins/.m2/")
    ]) {

    node("myJenkins") {

        @Library('github.com/redhat-helloworld-msa/jenkins-library@master') _
        
        stage ('SCM checkout'){
            echo 'Checking out git repository'
            checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[url: 'https://github.com/satnami/openshift-jenkins']]])
        }
        
        stage ('Swagger Dependencies'){
            echo 'Downloading swagger codegen lib'
            sh 'curl http://central.maven.org/maven2/io/swagger/swagger-codegen-cli/2.2.3/swagger-codegen-cli-2.2.3.jar >  swagger-codegen-cli.jar'
        }
        
        stage ('Swagger Code Generation'){
            echo 'Generating spring code'
            sh 'java -jar swagger-codegen-cli.jar generate -i swagger2.json -l spring -o ./'
        }
        
        stage ('Maven build'){
            echo 'Building project'
            sh 'mvn package'
        }
        
        stage ('DEV - Image build'){
            echo 'Building docker image and deploying to Dev'
            buildApp('jenkins-openshift-dev', 'server')
            echo "This is the build number: ${env.BUILD_NUMBER}"
        }
        
        stage ('Automated tests'){
            echo 'This stage simulates automated tests'
            env.NODEJS_HOME = "${tool 'Node 8.7.0'}"
            env.PATH="${env.NODEJS_HOME}/bin:${env.PATH}"
            sh 'node --version'
            sh 'npm --version'
            sh 'npm install -g newman'
            //sh 'newman run billingManagement.json.postman_collection --environment env.json'
        }
        
        stage ('QA - Promote image'){
            echo 'Deploying to QA'
            promoteImage('jenkins-openshift-dev', 'jenkins-openshift-qa', 'server', 'latest')
        }
    
        stage ('Wait for approval'){
            input 'Approve to production?'
        }
    
        stage ('PRD - Promote image'){
            echo 'Deploying to production'
            promoteImage('jenkins-openshift-qa', 'jenkins-openshift', 'server', env.BUILD_NUMBER)
        }

        stage ('PRD - Canary Deploy'){
            echo 'Performing a canary deployment'
            canaryDeploy('jenkins-openshift', 'server', env.BUILD_NUMBER)
        }
    }
}
