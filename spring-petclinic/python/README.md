### This is a quick demo of how to build, test, and publish the "spring-petclinic" app using Dagger with Python SDK.

##### Clone this repo
```git clone https://github.com/pavel-krizskii/dagger-examples.git```

##### Change working dir
```cd dagger-examples/spring-petclinic/python```

##### Install Dagger
```curl -fsSL https://dl.dagger.io/dagger/install.sh | DAGGER_VERSION=0.18.2 BIN_DIR=./.venv/dagger sh```

##### Prepend PATH with installed Dagger
```export PATH=./.venv/dagger:$PATH```

##### Run Dagger Shell
```dagger```

##### Build (from Dagger Shell)
```build-env```

##### Run tests (from Dagger Shell)
```test```

##### Publish to temp registry (from Dagger Shell)
```publish```

##### Run published app (localhost:8080)
```docker run --rm -p 8080:8080 ttl.sh/spring-petclinic-<id>```