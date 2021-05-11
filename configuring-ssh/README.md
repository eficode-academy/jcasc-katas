# Configuring SSH setup of Jenkins

SSH is the standard for a lot of things in a Unix/Linux environment. It's used
for [git](https://git-scm.com/), for quickly logging into servers without
passwords, and can also be used transfer files over the SSH protocol.

In this exercise, we will use several files and folders to configure Jenkins:

* `casc-config/jenkins.yaml` file for JCasC configuration of Jenkins.
* `docker-compose.yml` file for controlling our Jenkins docker instance.

We're continuing from [basic system config](../basic-system-config/README.md)
to configure Jenkins to use SSH. The docker-compose file has been prepared to
bootstrap an instance of Jenkins with some plugins.

This kata makes use of the
[ssh-credentials](https://plugins.jenkins.io/ssh-credentials/) plugin. When
starting the docker container, this plugin will be automatically installed.

## Tasks

The first task is to configure an SSH key. Please create a new SSH key for this
using the credentials plugin. Both [ed25519](https://ed25519.cr.yp.to/) and
[RSA](https://en.wikipedia.org/wiki/RSA_(cryptosystem)) algorithms are supported,
you can freely pick either.

```bash
mkdir keys
ssh-keygen -f ~/keys/jcasc-key -t ecdsa -b 521 -P ""
```

Next, you can post the private key into `.env`. If you run into YAML problems,
you can use the [YAML Multiline tool](https://yaml-multiline.info/).

With SSH, the private key should always be considered carefully, just like the
password for the authentication of the admin user in the [basic system config
kata](../basic-system-config/README.md). You can use a service like
[Vault](https://www.vaultproject.io/) for this purpose. It's not needed to set
a passphrase on the SSH key, and we'll skip it for simplicity. Add this snippet
to the `jenkins.yml`

```
system:
  domainCredentials:
      credentials:
        - basicSSHUserPrivateKey:
            scope: SYSTEM
            id: ssh_with_passphrase_provided
            username: ssh_root
            description: "SSH passphrase with private key file."
            privateKeySource:
              directEntry:
                privateKey: ${SSH_PRIVATE_KEY}

```

You can add the above to `jenkins.yml`, and then reload the
configuration. Hopefully, you'll see an entry under **Manage Jenkins** ->
**Manage Credentials**.

In Github, you can temporarily add the new public key under the , and try to
change a job to use the `ssh://` protocol. We can continue adding a small
job to see that it's possible to clone a repository using the new key.

### Extra exercises

As a bonus, you can add
[publish-over-ssh](https://plugins.jenkins.io/publish-over-ssh/) to the list of
plugins that JCasC installs. This plugin will allow you to transfer files as
part of the job to a remote server, using the SSH protocol.

* Talk about setup of [ssh key](https://help.github.com/en/github/authenticating-to-github/connecting-to-github-with-ssh)
* Paste in SSH key

## Clean up

To properly clean up after this exercise, we will stop and remove the container completely:

```bash
docker-compose down
```

## Further readings

If you're curious, we've selected a few interesting pages.

* [Jenkins wiki about SSH](https://wiki.jenkins.io/display/JENKINS/Jenkins+SSH)
* [Mozilla's infosec page on OpenSSH configuration](https://infosec.mozilla.org/guidelines/openssh.html)
