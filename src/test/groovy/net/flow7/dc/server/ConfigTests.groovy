/*
 * Copyright 2012 daviddale.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.flow7.dc.server;

import java.io.File;
import static org.junit.Assert.*;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 *
 * @author daviddale
 */
public class ConfigTests {
    
    Logger log = LoggerFactory.getLogger(ConfigTests.class);
    
    //@Test
    public void test_reading_config_file(){        
        File file = new File( Config.class.getResource( "test-config.properties" ).getFile()  );
        Config.setConfigFile( file );
        
        Config c = Config.get();
        
        log.info("Configured drives directory " + Config.get().prop("drives.directory"));
        log.info("Mail password " + Config.get().env("MAIL_PASS"));
        
        assertNotNull( Config.get().prop("drives.directory") );        
        
    }
    
    
}
