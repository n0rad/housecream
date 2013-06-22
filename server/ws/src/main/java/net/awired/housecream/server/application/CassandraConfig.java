/**
 *
 *     Housecream.org project
 *     Copyright (C) Awired.net
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package net.awired.housecream.server.application;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.awired.core.io.PortFinder;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CassandraConfig {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final List<String> defaultLines;

    private Integer storagePort;
    private Integer thriftPort;
    private Integer cqlPort;

    public CassandraConfig() {
        List<String> lines = new ArrayList<>();
        lines.add("cluster_name: 'Test Cluster'");
        lines.add("initial_token:");
        lines.add("hinted_handoff_enabled: true");
        lines.add("max_hint_window_in_ms: 10800000 # 3 hours");
        lines.add("hinted_handoff_throttle_in_kb: 1024");
        lines.add("max_hints_delivery_threads: 2");
        lines.add("authenticator: org.apache.cassandra.auth.AllowAllAuthenticator");
        lines.add("authorizer: org.apache.cassandra.auth.AllowAllAuthorizer");
        lines.add("permissions_validity_in_ms: 2000");
        lines.add("partitioner: org.apache.cassandra.dht.Murmur3Partitioner");
        lines.add("data_file_directories:");
        lines.add("    - target/embeddedCassandra/data");
        lines.add("commitlog_directory: target/embeddedCassandra/commitlog");
        lines.add("disk_failure_policy: stop");
        lines.add("key_cache_size_in_mb:");
        lines.add("key_cache_save_period: 14400");
        lines.add("row_cache_size_in_mb: 0");
        lines.add("row_cache_save_period: 0");
        lines.add("row_cache_provider: SerializingCacheProvider");
        lines.add("saved_caches_directory: /var/lib/cassandra/saved_caches");
        lines.add("commitlog_sync: periodic");
        lines.add("commitlog_sync_period_in_ms: 10000");
        lines.add("commitlog_segment_size_in_mb: 32");
        lines.add("seed_provider:");
        lines.add("    - class_name: org.apache.cassandra.locator.SimpleSeedProvider");
        lines.add("      parameters:");
        lines.add("          - seeds: \"127.0.0.1\"");
        lines.add("flush_largest_memtables_at: 0.75");
        lines.add("reduce_cache_sizes_at: 0.85");
        lines.add("reduce_cache_capacity_to: 0.6");
        lines.add("concurrent_reads: 32");
        lines.add("concurrent_writes: 32");
        lines.add("memtable_flush_queue_size: 4");
        lines.add("trickle_fsync: false");
        lines.add("trickle_fsync_interval_in_kb: 10240");
        lines.add("storage_port: 7000");
        lines.add("ssl_storage_port: 7001");
        lines.add("listen_address: 127.0.0.1");
        lines.add("start_native_transport: true");
        lines.add("native_transport_port: 9042");
        lines.add("start_rpc: true");
        lines.add("rpc_address: localhost");
        lines.add("rpc_keepalive: true");
        lines.add("rpc_server_type: sync");
        lines.add("thrift_framed_transport_size_in_mb: 15");
        lines.add("thrift_max_message_length_in_mb: 16");
        lines.add("incremental_backups: false");
        lines.add("snapshot_before_compaction: false");
        lines.add("auto_snapshot: true");
        lines.add("column_index_size_in_kb: 64");
        lines.add("in_memory_compaction_limit_in_mb: 64");
        lines.add("multithreaded_compaction: false");
        lines.add("compaction_throughput_mb_per_sec: 16");
        lines.add("compaction_preheat_key_cache: true");
        lines.add("read_request_timeout_in_ms: 10000");
        lines.add("range_request_timeout_in_ms: 10000");
        lines.add("write_request_timeout_in_ms: 10000");
        lines.add("truncate_request_timeout_in_ms: 60000");
        lines.add("request_timeout_in_ms: 10000");
        lines.add("cross_node_timeout: false");
        lines.add("endpoint_snitch: SimpleSnitch");
        lines.add("dynamic_snitch_update_interval_in_ms: 100");
        lines.add("dynamic_snitch_reset_interval_in_ms: 600000");
        lines.add("dynamic_snitch_badness_threshold: 0.1");
        lines.add("request_scheduler: org.apache.cassandra.scheduler.NoScheduler");
        lines.add("index_interval: 128");
        lines.add("server_encryption_options:");
        lines.add("    internode_encryption: none");
        lines.add("    keystore: conf/.keystore");
        lines.add("    keystore_password: cassandra");
        lines.add("    truststore: conf/.truststore");
        lines.add("    truststore_password: cassandra");
        lines.add("client_encryption_options:");
        lines.add("    enabled: false");
        lines.add("    keystore: conf/.keystore");
        lines.add("    keystore_password: cassandra");
        lines.add("internode_compression: all");
        lines.add("inter_dc_tcp_nodelay: true");
        defaultLines = Collections.unmodifiableList(lines);
    }

    public CassandraConfig storagePort(int port) {
        storagePort = port;
        return this;
    }

    public CassandraConfig thriftPort(int port) {
        thriftPort = port;
        return this;
    }

    public CassandraConfig cqlPort(int port) {
        cqlPort = port;
        return this;
    }

    public void writeToFile(File file) {
        log.info("Write Cassandra configuration file :" + file);
        try {
            FileUtils.writeLines(file, prepareLines());
        } catch (IOException e) {
            throw new IllegalStateException("Cannot write cassandra config lines to file : " + file);
        }
    }

    /////////////////////////////////////////////

    private List<String> prepareLines() {
        List<String> res = new ArrayList<>(defaultLines);
        res.add("storage_port: " + (storagePort == null ? PortFinder.findAvailableBetween(7001, 7991) : storagePort));
        res.add("rpc_port: " + (thriftPort == null ? PortFinder.findAvailableBetween(9161, 9961) : thriftPort));
        res.add("native_transport_port: " + (cqlPort == null ? PortFinder.findAvailableBetween(9043, 9160) : cqlPort));
        return res;
    }
}
