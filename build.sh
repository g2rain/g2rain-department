#!/usr/bin/env bash
set -e

# 切到脚本所在目录（确保在 g2rain-department 根目录执行）
cd "$(dirname "$0")"

APP_IMAGE="${APP_IMAGE:-ghcr.io/g2rain/g2rain-department}"

# 发布镜像不使用 latest；本地 Docker daemon 构建也必须显式指定版本或测试标签。
TAG="${1:?Usage: ./build.sh <tag>}"

echo "Building Docker image: ${APP_IMAGE}:${TAG}"

# 先在根目录编译并安装整个项目（包括所有子模块）
mvn -DskipTests=true clean install

# 然后进入 startup 子模块，执行 Jib 构建
cd g2rain-department-startup

mvn -DskipTests=true \
  compile jib:dockerBuild \
  -Djib.to.image=${APP_IMAGE}:${TAG}
