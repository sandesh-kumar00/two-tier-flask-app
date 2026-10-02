# Two-Tier Flask App — DevOps CI/CD Project

This project demonstrates the **containerization, security scanning, CI/CD automation, and Kubernetes deployment** of an existing two-tier Flask/MySQL application.

> The application code was pre-existing. This project focuses on the **DevOps implementation and cloud deployment workflow**.

## Architecture

```text
Developer
   |
   v
GitHub Repository
   |
   v
GitHub Actions
   |
   +--> SonarQube
   |      Code Analysis
   |
   +--> Docker Build
   |
   +--> Trivy
   |      Container Vulnerability Scan
   |
   v
Google Artifact Registry
   |
   v
GKE
   |
   v
Kubernetes Deployment
   |
   +--> Flask Application
   |
   +--> MySQL
```

## Tech Stack

* Flask / Python
* MySQL
* Docker
* Docker Compose
* Kubernetes
* Google Kubernetes Engine (GKE)
* Google Artifact Registry
* GitHub Actions
* Workload Identity Federation (WIF)
* SonarQube
* Trivy
* Google Cloud IAM

## My DevOps Contribution

* Created Docker configuration and containerized the application.
* Configured Docker Compose for the Flask and MySQL services.
* Created Kubernetes deployment configuration.
* Configured Google Artifact Registry for container images.
* Built the GitHub Actions CI/CD pipeline.
* Implemented keyless GitHub-to-GCP authentication using Workload Identity Federation.
* Added SonarQube for source-code quality analysis.
* Added Trivy for Docker image vulnerability scanning.
* Used Git commit SHA as the container image tag.
* Automated image push to Artifact Registry.
* Automated deployment to GKE using `kubectl set image`.
* Added Kubernetes rollout verification using `kubectl rollout status`.
* Configured required GCP IAM permissions and service accounts.

## CI/CD Pipeline

```text
Git Push to main
       |
       v
Checkout
       |
       v
SonarQube Scan
       |
       v
Docker Build
       |
       v
Trivy Image Scan
       |
       v
Push to Artifact Registry
       |
       v
Authenticate to GCP using WIF
       |
       v
Deploy to GKE
       |
       v
Rollout Verification
```

## Security

### Workload Identity Federation

GitHub Actions authenticates to Google Cloud using **Workload Identity Federation** instead of storing long-lived service-account JSON keys in GitHub.

### SonarQube

SonarQube is used for static code analysis and Quality Gate enforcement.

The pipeline proceeds to the Docker build only when the configured Quality Gate passes.

### Trivy

Trivy scans the built Docker image before it is pushed to Artifact Registry.

The pipeline is configured to fail on **HIGH and CRITICAL** vulnerabilities.

## Container Image

```text
REGION-docker.pkg.dev/PROJECT_ID/REPOSITORY/IMAGE:GIT_SHA
```

Using the Git commit SHA gives each build a unique and traceable image version.

## Kubernetes Deployment

Initial deployment:

```bash
kubectl apply -f k8s/
```

Subsequent image update:

```bash
kubectl set image deployment/app-deploy \
  app-deploy=REGION-docker.pkg.dev/PROJECT_ID/REPOSITORY/IMAGE:GIT_SHA \
  -n NAMESPACE
```

Rollout verification:

```bash
kubectl rollout status deployment/app-deploy \
  -n NAMESPACE \
  --timeout=5m
```

## Local Setup

```bash
git clone https://github.com/sandesh-kumar00/two-tier-flask-app.git
cd two-tier-flask-app
docker compose up -d
```

Application:

```text
http://localhost:5000
```

## CI/CD Requirements

* GCP Workload Identity Federation provider
* Dedicated GCP deployment service account
* Artifact Registry repository
* GKE cluster
* `SONAR_TOKEN` GitHub secret
* `SONAR_HOST_URL` GitHub repository variable

No long-lived GCP service-account JSON key is required.


## Project Goal

The goal of this project is to demonstrate an end-to-end **DevOps CI/CD workflow on GCP**:

**Code → Security Scan → Containerization → Artifact Registry → Kubernetes → GKE**
