package org.sonar.plugins.yaml.checks;

import junit.framework.TestCase;

public class EmptyLinesCheckTest extends TestCase {
    public void testCheck() {
        assertNotNull(new EmptyLinesCheck());
    }
}