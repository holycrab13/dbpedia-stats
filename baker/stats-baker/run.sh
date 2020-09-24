#!/usr/bin/env bash
cd /stats-baker/target
java -jar stats-baker-0.0.1-SNAPSHOT.jar -c ${COLLECTION_URI} -stats-file ${STATS_DIR}/stats.json

