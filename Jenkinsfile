pipeline {
  agent any
  stages {
    stage('Setup') {
        failFast true
        parallel {
            stage ('Emulator Setup') {
              steps {
                sh 'emulator -list-avds'
                sh 'emulator -avd Automation_Test_API_24 -no-boot-anim -no-window -noaudio -no-snapshot-save &'
                echo '------ Emulator starting ------'
                sh 'sleep 10' // sleep for 10s
                sh 'adb devices'
              }
            }
            stage ('Clean') {
              steps {
                sh './gradlew clean'
              }
            }
        }
    }
    stage ('Integration Test') {
      steps {
        sh "./gradlew connectedAndroidTest"
      }
    }
  }
}
