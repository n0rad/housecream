#!/dgr/bin/busybox sh
set -e
. /dgr/bin/functions.sh
isLevelEnabled "debug" && set -x


mkdir -p ${ROOTFS}/usr/bin
find /code/server/dist/ -type d -name 'housecream-*linux-amd64' -exec cp {}/housecream ${ROOTFS}/usr/bin \;

VERSION=$(${ROOTFS}/usr/bin/housecream -V | grep Version | cut -f2 -d: | tr -d '[:space:]')
echo -e "default:\n  version: ${VERSION}" > /dgr/builder/attributes/version.yml
