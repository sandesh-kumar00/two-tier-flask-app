def call(String projectName, String imageTag, String dockerHubUser, String dockerfilePath = "Dockerfile", String contextPath = ".") {
    sh "docker build -t ${dockerHubUser}/${projectName}:${imageTag} -f ${dockerfilePath} ${contextPath}"
}
