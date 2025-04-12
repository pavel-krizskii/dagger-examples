### This is a quick demo of how to create, test, and publish a simple Python application using Dagger.

##### Clone this repo
```git clone https://github.com/pavel-krizskii/dagger-examples.git```

##### Change working dir
```cd dagger-examples/python-app```

##### Create a virtual environment
```python3 -m venv .venv```

##### Activate virtual environment
```source .venv/bin/activate```

##### Install dependencies
```pip install -r requirements.txt```

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

##### Run published app
```docker run --rm ttl.sh/python-app-<id>```