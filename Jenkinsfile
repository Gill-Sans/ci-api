pipeline {
	agent any
    tools {
		jfrog 'jfrog-cli-latest'
    }
    stages {
		stage('Checkout') {
			steps {
				checkout([
                    $class: 'GitSCM',
                    branches: [[name: "*/dev"]],
                    doGenerateSubmoduleConfigurations: false,
                    extensions: [
                        [$class: 'PathRestriction',
                            includedRegions: 'capit-schedule/.*\ncapit-common/.*'
                        ]
                    ],
                    submoduleCfg: [],
                    userRemoteConfigs: [[url: 'https://github.com/Gill-Sans/ci-api.git']]
                ])
            }
        }
        stage('Build') {
			steps {
				sh "cd capit-schedule && mvn clean install -B -DskipTests --settings ~/.m2/settings.xml"
            }
        }
        stage('Upload Artifact') {
			steps {
				sh "jfrog rt u \"capit-schedule/target/*.jar\" libs-snapshot-local --build-name=capit-schedule --build-number=\${BUILD_NUMBER}"
            }
        }
    }
    post {
		success {
			echo "Build & Deploy succeeded for capit-schedule!"
        }
        failure {
			echo "Build & Deploy failed. Check the logs for issues."
        }
    }
}