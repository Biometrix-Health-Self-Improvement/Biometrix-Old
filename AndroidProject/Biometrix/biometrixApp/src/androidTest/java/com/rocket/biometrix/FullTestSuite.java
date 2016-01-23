package com.rocket.biometrix;

import android.test.suitebuilder.TestSuiteBuilder;

import junit.framework.Test;
import junit.framework.TestSuite;
//TODO: Write example JUnit tests, Expresso, and Roboelectric if possible, if not - just built in UI test :(
/**
 * Created by JP on 1/21/2016.
 *
 */
public class FullTestSuite extends TestSuite {
    public static Test suite(){
        return new TestSuiteBuilder(FullTestSuite.class)
                .includeAllPackagesUnderHere().build();
    }

    public FullTestSuite(){
        super();
    }

}


/* within android hash
*testOptions.unitTests.all {
    testLogging {
        events 'passed', 'skipped', 'failed', 'standardOut', 'standardError'
        outputs.upToDateWhen { false }
        showStandardStreams = true
    }
}
 */