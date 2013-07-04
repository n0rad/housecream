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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import org.apache.cassandra.config.Config;
import org.apache.cassandra.config.Config.CommitLogSync;
import org.apache.cassandra.config.Config.DiskFailurePolicy;
import org.apache.cassandra.config.Config.InternodeCompression;
import org.apache.cassandra.config.EncryptionOptions.ServerEncryptionOptions.InternodeEncryption;
import org.apache.cassandra.config.SeedProviderDef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Loader;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;

public class CassandraConfig {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final File cassandraHome;
    private final File configFile;

    private Config config = new Config();

    public CassandraConfig(String clusterName, File cassandraHome) {
        this.cassandraHome = cassandraHome;
        this.configFile = new File(cassandraHome, "config.yaml");

        //        lines.add("seed_provider:");
        //        lines.add("    - class_name: org.apache.cassandra.locator.SimpleSeedProvider");
        //        lines.add("      parameters:");
        //        lines.add("          - seeds: \"127.0.0.1\"");
        //
        //        lines.add("storage_port: 7000");
        //        lines.add("ssl_storage_port: 7001");
        //        lines.add("listen_address: 127.0.0.1");
        //
        //        lines.add("start_native_transport: true");
        //        lines.add("native_transport_port: 9042");
        //        lines.add("start_rpc: true");
        //        lines.add("rpc_address: localhost");
        //        lines.add("rpc_keepalive: true");
        //        lines.add("rpc_server_type: sync");

        config.cluster_name = clusterName;
        //        config.initial_token = "";
        config.hinted_handoff_enabled = true;
        config.max_hint_window_in_ms = 10800000; // 3 hours
        config.hinted_handoff_throttle_in_kb = 1024;
        config.max_hints_delivery_threads = 2;
        config.authenticator = org.apache.cassandra.auth.AllowAllAuthenticator.class.getName();
        config.authorizer = org.apache.cassandra.auth.AllowAllAuthorizer.class.getName();
        config.permissions_validity_in_ms = 2000;
        config.partitioner = org.apache.cassandra.dht.Murmur3Partitioner.class.getName();
        config.disk_failure_policy = DiskFailurePolicy.stop;
        //        config.key_cache_size_in_mb ="";
        config.key_cache_save_period = 14400;
        config.row_cache_size_in_mb = 0;
        config.row_cache_save_period = 0;
        config.row_cache_provider = "SerializingCacheProvider";
        //            config.saved_caches_directory= /var/lib/cassandra/saved_caches;
        config.commitlog_sync = CommitLogSync.periodic;
        config.commitlog_sync_period_in_ms = 10000;
        config.commitlog_segment_size_in_mb = 32;
        LinkedHashMap<String, Object> hashMap = new LinkedHashMap<>();
        hashMap.put("class_name", org.apache.cassandra.locator.SimpleSeedProvider.class.getName());
        //        Map<String, String> hashMap2 = new LinkedHashMap<>();
        //        hashMap2.put("seeds", "127.0.0.1");
        //        hashMap.put("parameters", Arrays.asList(hashMap2));
        //        config.seed_provider = new SeedProviderDef(hashMap);
        config.flush_largest_memtables_at = 0.75;
        config.reduce_cache_sizes_at = 0.85;
        config.reduce_cache_capacity_to = 0.6;
        config.concurrent_reads = 32;
        config.concurrent_writes = 32;
        config.memtable_flush_queue_size = 4;
        config.trickle_fsync = false;
        config.trickle_fsync_interval_in_kb = 10240;
        config.storage_port = 7000;
        config.ssl_storage_port = 7001;
        config.listen_address = "127.0.0.1";
        config.start_native_transport = true;
        config.native_transport_port = 9042;
        config.start_rpc = true;
        config.rpc_address = "localhost";
        config.rpc_keepalive = true;
        config.rpc_server_type = "sync";
        config.thrift_framed_transport_size_in_mb = 15;
        config.incremental_backups = false;
        config.snapshot_before_compaction = false;
        config.auto_snapshot = true;
        config.column_index_size_in_kb = 64;
        config.in_memory_compaction_limit_in_mb = 64;
        config.multithreaded_compaction = false;
        config.compaction_throughput_mb_per_sec = 16;
        config.compaction_preheat_key_cache = true;
        config.read_request_timeout_in_ms = 10000L;
        config.range_request_timeout_in_ms = 10000L;
        config.write_request_timeout_in_ms = 10000L;
        config.truncate_request_timeout_in_ms = 60000L;
        config.request_timeout_in_ms = 10000L;
        config.cross_node_timeout = false;
        config.endpoint_snitch = "SimpleSnitch";
        config.dynamic_snitch_update_interval_in_ms = 100;
        config.dynamic_snitch_reset_interval_in_ms = 600000;
        config.dynamic_snitch_badness_threshold = 0.1;
        config.request_scheduler = org.apache.cassandra.scheduler.NoScheduler.class.getName();
        config.index_interval = 128;
        config.server_encryption_options.internode_encryption = InternodeEncryption.none;
        //        config.server_encryption_options.keystore = "conf/.keystore";
        config.server_encryption_options.keystore_password = "cassandra";
        //        config.server_encryption_options.truststore = "conf/.truststore";
        config.server_encryption_options.truststore_password = "cassandra";
        config.client_encryption_options.enabled = false;
        //        config.client_encryption_options.keystore = "conf/.keystore";
        config.client_encryption_options.keystore_password = "cassandra";
        config.internode_compression = InternodeCompression.all;
        config.inter_dc_tcp_nodelay = true;

    }

    private void updateWithHomePath(File cassandraHome) {
        String absolutePath = cassandraHome.getAbsolutePath();

        config.client_encryption_options.keystore = absolutePath + "/keystore";
        config.data_file_directories = new String[] { absolutePath + "/data" };
        config.commitlog_directory = absolutePath + "/commitlog";
        config.server_encryption_options.keystore = absolutePath + "/keystore";
        config.server_encryption_options.truststore = absolutePath + "/truststore";
        config.client_encryption_options.keystore = absolutePath + "/keystore";
        config.saved_caches_directory = absolutePath + "saved_caches";
    }

    public void load() {
        try (FileInputStream stream = new FileInputStream(configFile)) {
            config = (Config) getYaml().load(stream);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot write Cassandra configuration file : " + configFile, e);
        }
    }

    public void write() {
        log.info("Write Cassandra configuration file :" + configFile);
        updateWithHomePath(cassandraHome);
        try {
            configFile.getParentFile().mkdirs();
            configFile.createNewFile();
        } catch (IOException e1) {
            throw new IllegalStateException("Cannot create config file", e1);
        }
        try (PrintWriter printWriter = new PrintWriter(configFile)) {
            String data = getYaml().dump(config);
            printWriter.println(data);
            printWriter.println();
            printWriter.println("seed_provider:");
            printWriter.println("    - class_name: org.apache.cassandra.locator.SimpleSeedProvider");
            printWriter.println("      parameters:");
            printWriter.println("          - seeds: \"127.0.0.1\"");

        } catch (FileNotFoundException e) {
            throw new IllegalStateException("Cannot write Cassandra configuration to file : " + configFile, e);
        }
    }

    /////////////////////////////////////////////

    private Yaml getYaml() {
        org.yaml.snakeyaml.constructor.Constructor constructor = new org.yaml.snakeyaml.constructor.Constructor(
                Config.class);
        TypeDescription seedDesc = new TypeDescription(SeedProviderDef.class);
        seedDesc.putMapPropertyType("parameters", String.class, String.class);
        constructor.addTypeDescription(seedDesc);
        Yaml yaml = new Yaml(new Loader(constructor));
        return yaml;
    }

    public File getConfigFile() {
        return configFile;
    }

    public int getCqlPort() {
        return config.native_transport_port;
    }

    public String getCqlHost() {
        return config.listen_address;
    }

    //    res.add("storage_port: " + (storagePort == null ? PortFinder.findAvailableBetween(7001, 7991) : storagePort));
    //    res.add("rpc_port: " + (thriftPort == null ? PortFinder.findAvailableBetween(9161, 9961) : thriftPort));
    //    res.add("native_transport_port: " + (cqlPort == null ? PortFinder.findAvailableBetween(9043, 9160) : cqlPort));

}