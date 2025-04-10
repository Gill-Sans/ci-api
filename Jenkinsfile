pipeline {
	agent any
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

        stage('Build & Deploy') {
			steps {
                sh "mvn clean deploy -X -pl capit-schedule -am -B -DskipTests --settings ~/.m2/settings.xml"
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