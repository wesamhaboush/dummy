package com.codebreeze.incubator.logging;

import com.jpmorgan.dcpp.commons.logging.slf4j.Slf4jLoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.jpmorgan.dcpp.commons.Randoms.randomAlphanumeric;

/**
 * provide useful info in the log to help debug/mitigate/recover issues
 * No Logging at all is cruel and business-death sentence
 * use appropriate level (error, warn, info, debug, trace)
 * use parameterized logging
 * declare loggers appropriately
 * watch for null pointer exceptions (request.getId()?? what if request is null)
 *      BeanUtil.getProperty(request, "id")
 * watch when you log collections:
 * * Out of memory error,
 * * N+1 select problem (do not cause a lot of sequential access on printing a batch of items)
 * * Thread starvation (if logging is synchronous!),
 * * Lazy initialization exception - potentially expensive, potentially unnecessary or unintended, and potentially disastrous,
 * * Logs storage filled completely, how many items?
 * Instead, with collections, log size, first few items, last few items, a sample, some meta data.
 * Make sure toString is implemented for logged classes
 * Watch for arrays, they have no toString
 * Be consistent to allow grepping and manipulation (parsing and reading) (tradeId vs trade id vs id of trade vs trade with id)
 * use a demarcations delimiters to be clear about the content.
 *  * user said [nothing]
 *  * user said nothing []
 *  * vs
 *  * user said nothing
 * No side effects in log statements.
 * Do NOT log sensitive information
 * avoid logging exceptions, let your framework or container (whatever it is) do it for you
 */

public class LoggingBestPractices {
    //this is a good way to declare a logger
    private static final Logger LOG1 = LoggerFactory.getLogger(LoggingBestPractices.class);
    //this is even better
    private static final Logger LOG = Slf4jLoggerFactory.create();

    // DO: use string literals, and no complex evaluation computation and no string concatenation
    public void simpleLogsOfValues(Something something) {
        LOG.debug("method invoked with arguments [{}]", something);
    }

    // DONT: concatenation which means a new string with every invocation
    public void iAmExpensiveInTermsOfGc(Something something) {
        LOG.debug("Method invoked with arguments [" + something + "]");
    }

    // DONT: code clutter and eye pain
    public void iAmMoreComplicatedThanINeedToBe(Something something) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Method invoked with arguments {}", something);
        }
    }

    // DONT: may be trace is better?
    public void tooLowLevelMessagesInDebugAndInfo(Something something) {
        LOG.debug("arg is {}", something);
    }

    // GOOD: correct way to log it (without the braces)
    public void correctlyLoggedException(Something something) {
        try {
            doSomething(something);
        } catch (SomeException ex) {
            LOG.warn("Failed to do something with {}, continuing ", something, ex);
        }
    }

    // DONT:
    // - exception treated as an object
    // - exception's cause is gone
    // - whole stack trace is gone
    public void iAmIncorrectlyLogged(Something something) {
        try {
            doSomething(something);
        } catch (SomeException ex) {
            LOG.warn("Failed to do something with {} because {}, continuing", something, ex);
        }
    }

    // DONT:
    // - no context given
    // - evaluation of toString always happens. Can be expensive. Even if debug is off!
    // - hopefully toString is useful, but may be we could ask for what we want (like getTradeId, getEventId, etc)
    public void probablyNotUseful(Something something) {
        LOG.debug(something.toString());
    }

    // DONT:
    // - no context given
    // - hopefully toString is useful
    public void bad_method2(Something something) {
        LOG.debug("[{}]", something);
    }

    // DONT:
    // - silently ignoring errors when debug/info is NOT enabled!!
    public void iSometimesAllowErrorsThrough(Something something) {
        try {
            doSomething(something);
        } catch (SomeException ex) {
            LOG.debug("error occurred with something [{}]", something, ex);
            LOG.info("error occurred with something [{}]", something, ex);
        }
    }

    // DONT:
    // - always toString evaluated. Expensive.
    // - no context given
    // - cause is gone
    // - stack trace is gone
    // - hopefully cheap and helpful toString in the second
    // - In number 3, the stack trace will tell you it is an exception. 'Exception' is useless
    public void iLogLosingAlotOfInfo(Something something) {
        try {
            doSomething(something);
        } catch (SomeException ex) {
            LOG.warn(ex.getMessage());
            LOG.warn(ex.toString());
            LOG.warn("Exception {}", ex);
        }
    }

    //DO:
    // - information you want to give is:
    //        what is it that has gone wrong?
    //        how critical this is?
    //        are you able to recover? and what possible action can be taken? and whether we managed to recover?

    // DO:
    // - use string literals (doing something with something)
    // - what was being attempted when the error occurred.
    // - any contextual information about what was being attempted (pay somebody, annul a trade, etc) - the something
    // - information on how the code is going to cope with this error (ignore here)
    public void good_method1(Something something) {
        try {
            doSomething(something);
        } catch (SomeException ex) {
            LOG.warn("Failed to do something with {}, ignoring it because other means to get this done are available", something, ex);
            LOG.error("Failed to do something with {}, escalating to operations", something, ex);
            sendEmailToOperations("doing something");
        }
    }

    private void areAllOfUsWrong() {
        try {
            Integer x = null;
            ++x;
        } catch (Exception e) {
            LOG.error("" + e);        //C
            LOG.error(e.toString());        //D
            LOG.error(e.getMessage());        //E
            LOG.error(null, e);        //F
            LOG.error("", e);        //G
            LOG.error("{}", e);        //H
            LOG.error("{}", e.getMessage());        //I
            LOG.error("Error reading configuration file: " + e);        //J
            LOG.error("Error reading configuration file: " + e.getMessage());        //K
            LOG.error("Error reading configuration file", e);        //L
        }
    }

    private void arraysHandling() {
        final Integer[] ages = {1, 2, 3};
        final Integer[][] agesMulti = {{2}, {1, 2}, {3, 4, 5, 6, 7}};
        LOG.info("team ages {}", ages); //almost never
        LOG.info("team ages {}", Arrays.asList(ages)); // single dimension
        LOG.info("team ages {}", Arrays.toString(ages)); // single dimension -- use the asList in preference to this
        //because it will not creat ea new string if logging is not required (as if debug or logging is disabled for this package)
        LOG.info("team ages {}", Arrays.deepToString(ages)); // multi-dimensional and unknown
    }

    /**
     * DONT:
     * - people will confuse it and remove hte logging
     * - always ask or do, do not do both
     * - coupling introduced, and humanly confusing
     */
    private void sideEffects() {
        final Integer[] ages = {1, 2, 3};
        LOG.info("team ages {}", increment(ages)); //
    }

    /**
     * DONT:
     * - Not everything should be logged
     */
    private void sensitive() {
        final String password = "**********";
        final String pinNumber = "0000";
        final int weight = Integer.MAX_VALUE;
        LOG.info(
                "my password {}, " +
                "my pin number {}, " +
                 "my weight {}", password, pinNumber, weight); //
    }

    //utils
    private static class Something {
        @Override
        public String toString() {
            return randomAlphanumeric(10);
        }
    }

    private Integer[] increment(Integer[] ages) {
        final List<Integer> newAges = new ArrayList(ages.length);
        for(Integer age: ages){
            newAges.add(++age);
        }
        return newAges.toArray(new Integer[ages.length]);
    }

    private static class SomeException extends Exception {

    }

    private void sendEmailToOperations(final String subject) {
        LOG.info("sending email to operations with regard to {}", subject);
    }

    private void doSomething(final Something something) throws SomeException {
        LOG.info("the following thing is being done {}", something);
    }
}


