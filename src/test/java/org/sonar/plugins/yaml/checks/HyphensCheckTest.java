package org.sonar.plugins.yaml.checks;

import junit.framework.TestCase;

public class HyphensCheckTest extends TestCase {
    public void testCheck() {
        assertNotNull(new HyphensCheck());
    }
}