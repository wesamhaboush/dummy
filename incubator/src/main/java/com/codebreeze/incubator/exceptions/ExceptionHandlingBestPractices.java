package com.codebreeze.incubator.exceptions;

import com.google.common.base.Throwables;
import com.google.common.util.concurrent.Uninterruptibles;
import com.jpmorgan.dcpp.commons.logging.slf4j.Slf4jLoggerFactory;
import org.slf4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkArgument;
import static com.jpmorgan.dcpp.commons.IO.asCloseable;
import static com.jpmorgan.dcpp.commons.IO.closeQuietly;
import static com.jpmorgan.dcpp.commons.Randoms.randomBoolean;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.mockito.Mockito.mock;

/**
 * Never swallow the exception in catch block
 * Declare the specific checked/unchecked exceptions that your method can throw
 * Do not catch the Exception class rather catch specific sub classes
 * Never catch Throwable class
 * Always correctly wrap the exceptions in custom exceptions so that stack trace is not lost
 * Either log the exception or throw it but never do the both
 * Never throw any exception from finally block
 * Always catch only those exceptions that you can actually handle
 * Don't use printStackTrace() statement or similar methods
 * Use finally blocks instead of catch blocks if you are not going to handle exception
 * Remember "Throw early catch late" principle
 * Always clean up after handling the exception
 * Throw only relevant exception from a method
 * Never use exceptions for flow control in your program
 Validate user input to catch adverse conditions very early in request processing
 Always include all information about an exception in single log message
 Pass all relevant information to exceptions to make them informative as much as possible
 Always terminate the thread which it is interrupted
 Use template methods for repeated try-catch
 Document all exceptions in your application in javadoc
 */
public class ExceptionHandlingBestPractices {

    public void customException(final String id){
        final boolean tradeNotFound = true;
        if(tradeNotFound){
            throw new TradeException("Couldn't trade with id " + id);
        }
    }

    public void customExceptionWithStackTrace(final String id){
        try {
            Orcherstration
                    .create()
                    .then(Orchestrations.wait(5, TimeUnit.MINUTES))
                    .then(TIME_OUT);
        } catch (TimeoutException e){
            throw new TradeException("timed out search for trade " + id, e);
            // wrong: throw new TradeException("timed out search for trade " + id);
            // wrong: throw new TradeException("timed out search for trade " + id + " " + e.getMessage());
            // wrong: throw new TradeException("timed out search for trade " + id + " " + e.toString());
            // less wrong: throw new TradeException(e);
        }
    }

    public String neverSwallowInACatch(){
        try {
            return Orcherstration
                    .create()
                    .then(Orchestrations.wait(5, TimeUnit.MINUTES))
                    .then(TIME_OUT)
                    .then(getResult());
        } catch (TimeoutException e){
            return null;
            //return
            //e.printStackTrace()
            //worst is NOTHING
        }
    }

    public void neverThrowStones() throws Exception { //Ouch ouch ouch ..
    }

    public void neverCatchStones(final String id){
        try {
            Orcherstration
                    .create()
                    .then(Orchestrations.wait(5, TimeUnit.MINUTES))
                    .then(TIME_OUT);
        } catch (Exception e){ //worse is catching Throwable
            LOGGER.error("timed out search for trade " + id, e);
        }
    }

    public void neverLogAndThrow(final String id){
        try {
            Orcherstration
                    .create()
                    .then(Orchestrations.wait(5, TimeUnit.MINUTES))
                    .then(TIME_OUT);
        } catch (TimeoutException e){
            // log reader hell begins here!
            LOGGER.error("timed out search for trade " + id, e);
            throw new TradeException("time is out on trade" + id, e);
        }
    }

    public void onlyBiteWhatYouCanChew(){
        try {
            Orcherstration
                    .create()
                    .then(Orchestrations.wait(5, TimeUnit.MINUTES))
                    .then(TIME_OUT);
        } catch (TimeoutException e){ //worse is catching Throwable
            //eh ...
            throw e;
        }
    }

    public void maskExceptionsWhenNecessary(final String id){
        try {
            Orcherstration
                    .create()
                    .then(Orchestrations.wait(5, TimeUnit.MINUTES))
                    .then(TIME_OUT);
        } catch (TimeoutException e){
            //change low level to high level or from one domain to another
            throw new TradeException("timed out searching for trade info for trade " + id, e);
        }

        try {
            Orcherstration
                    .create()
                    .then(Orchestrations.wait(5, TimeUnit.MINUTES))
                    .then(TIME_OUT);
        } catch (Exception e){
            //stop bleeding (stop the propagation of damage to the rest of the upstream system)
            throw new TradeException("someone created an api that throws Exception. I have to deal with it while processing trade with id: " + id, e);
        }

        try {
            Orcherstration
                    .create()
                    .then(Orchestrations.wait(5, TimeUnit.MINUTES))
                    .then(TIME_OUT);
            someCodeThatThrowsTimeoutChecked();
        } catch (TimeoutCheckedException e){
            //make checked unchecked (some products use this policy)
            throw new TradeException("" + id, e); // or propagate from guava or similar
        }
    }

    public void doNotUsePrintStackTraceEver(){
        try {
            Orcherstration
                    .create()
                    .then(Orchestrations.wait(5, TimeUnit.MINUTES))
                    .then(TIME_OUT);
        } catch (TimeoutException e){
            //no information on what was going on,
            //what if the default console is disabled, etc.
            //it is just inflexible too. Use logs instead if you have to print cz you can configure (vs code)
            e.printStackTrace();
        }
    }

    public void finallyYouCanClean(){
        try {
            Orcherstration
                    .create()
                    .then(Orchestrations.wait(5, TimeUnit.MINUTES))
                    .then(TIME_OUT);
        } finally {
            letsCleanSomeResources();
        }
    }



    public void throwEarlyAndCatchLate(final String tradeId){
        shoutIfInvalide(tradeId); // validate inputs at boundaries and throw exceptions if validation fails before you go into more complex processing of corrupt data
        try {
            processNormally(tradeId);
        } catch (TradeException te){ // catch at the root of the processing code, not at the low level in 99% of cases
            handleTradeProcessingFailure(te);
        }
    }

    private void handleTradeProcessingFailure(TradeException te) {
        LOGGER.error("here, i am handling some failure by logging, which is not enough most of the time", te);
        //may send emails, retry, breakcircuit, backoff, etc
    }

    public void resourcesMustBeClosedAppropriate(final String id){
        ResultSet rs = getTradeInfoFromDatabase(id);;
        try {
            processNormally(rs);
            closeQuietly(asCloseable(rs)); // outchhhhhhhhhhhhhhhhhhhhhhhhhhh, we may never arrive here, so resources will start leaking
        } catch (TradeException te){
            handleTradeProcessingFailure(te); // dirty resources left in the sink!
        }


        try {
            processNormally(rs);
        } catch (TradeException te){
            handleTradeProcessingFailure(te);
        } finally {
            closeQuietly(asCloseable(rs)); // phewww .. resources are always cleaned and polished :)
        }
    }

    public User exceptionsAreNotForFlowControl(final String mayBeNumber){
        //explicit is good
        if(isBlank(mayBeNumber)){
            return userDidNotSupplyHisAge();
        } else if(isNotBlank(mayBeNumber)) {
            return userSuppliedHisAge(Integer.parseInt(mayBeNumber));
        }

        //using exception to do flow management is NOT good.
        try {
            return userSuppliedHisAge(Integer.parseInt(mayBeNumber));
        } catch (NumberFormatException te){
            return userDidNotSupplyHisAge(); //what if he supplied rubbish by loads?
        }
    }

    public void wakeUpEarly(final String tradeId, Trader aTrader, Operation op){
        try {
            shoutIfInvalide(tradeId);
            processNormally(tradeId);

            shoutIfInvalide(aTrader);
            processNormally(aTrader);

            shoutIfInvalide(op);
            processNormally(op);
        } catch (IllegalArgumentException e){
            rollBackAll();
        } catch (TradeException e){
            rollBackAll();
        }

        //vs.

        try {
            shoutIfInvalide(tradeId);
            shoutIfInvalide(op);
            shoutIfInvalide(aTrader);
            processNormally(tradeId);
            processNormally(aTrader);
            processNormally(op);
        } catch (TradeException e){
            rollBackAll();
        }

    }

    public void tellAll(final String tradeId, final Trader trader, final Operation op){
        try {
            processNormally(tradeId);
        } catch (TradeException e){
            LOGGER.error("error occurred");
            LOGGER.error("trade id {}", tradeId);
            LOGGER.error("trader {}", trader);
            LOGGER.error("Operation {}", op);
            //vs.
            LOGGER.error("error process trade id {}, trader {}, Operation {}", tradeId, trader, op);
        }
    }

    public void encapsulateComplexityAndDuplication(final String tradeId){
        try {
            processNormally(tradeId);
        } catch (TimeoutException e){
            handle(e);
        } catch (TradeException e){
            handle(e);
        } catch (RareException e){
            handle(e);
        }
    }

    public void keepAnEyeOnSizeAndComplexity(final String tradeId,
                                             final List<K> ks,
                                             final String d,
                                             final boolean a,
                                             final boolean b,
                                             final boolean c){
        final Connection connection = createConnection();
        boolean necessary = isNecessary();
        try {
            if(a){
                process(a);
            } else {
                process(b);
                if(c){
                    process(c);
                }
            }
            for(K k : ks){
                process(k);
            }
            try {
                process(d);
            } catch (TradeException e){
                ohMyGodProcessing();
            }
        } catch (TimeoutException e){
            sendEmailToOperations();
            logErrorMessageAppropriately(e);
            Uninterruptibles.sleepUninterruptibly(5, TimeUnit.HOURS);
            checkSleepingMadeYouComfortable();
            updateAnalyticsSystem();
            if(necessary){
                phoneEmergencyServices();
            }
        } catch (TradeException e){
            soundAlarm();
            Uninterruptibles.sleepUninterruptibly(5, TimeUnit.HOURS);
            soundLouderAlarm();
            if(!isFixed()){
                orderEvacuation();
            }
        } catch (RareException e){
            if(rareIsRare()){
                assumeItIsAGlitch();
                tryAgain();
            }
        } finally {
            if(connection != null){
                try {
                    connection.close();
                } catch (Exception e){
                    LOGGER.warn("could not close!! [{}]");
                    //ignore
                }
            }
        }

        try {
            processNormally(tradeId);
        } catch (TimeoutException e){
            handleTimeOut(e, tradeId, ks, d, a, b, c);
        } catch (TradeException e){
            handleTradeErrors(e, tradeId, ks, d, a, b, c);
        } catch (RareException e){
            handleGlitches(e, tradeId, ks, d, a, b, c);
        }
    }

    //Utils

    private void tryAgain() {

    }

    private void assumeItIsAGlitch() {

    }

    private boolean rareIsRare() {
        return false;
    }

    private void orderEvacuation() {

    }

    private void soundLouderAlarm() {

    }

    private void soundAlarm() {

    }

    private void phoneEmergencyServices() {

    }

    private void updateAnalyticsSystem() {

    }
    
    private void checkSleepingMadeYouComfortable() {
        
    }

    private void handleTimeOut(TimeoutException e, String tradeId, List<K> ks, String d, boolean a, boolean b, boolean c) {

    }

    private void handleTradeErrors(TradeException e, String tradeId, List<K> ks, String d, boolean a, boolean b, boolean c) {

    }

    private void handleGlitches(RareException e, String tradeId, List<K> ks, String d, boolean a, boolean b, boolean c) {

    }

    
    private static final Runnable TIME_OUT = new Runnable() {
        @Override
        public void run() {
            throw new TimeoutException("something weird");
        }
    };

    public boolean isFixed() {
        return randomBoolean();
    }

    private static class TimeoutException extends RuntimeException{
        public TimeoutException(String message) {
            super(message);
        }
        public TimeoutException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    private static class TimeoutCheckedException extends Exception{
        public TimeoutCheckedException() {
        }
        public TimeoutCheckedException(String message) {
            super(message);
        }
        public TimeoutCheckedException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    private static class TradeException extends RuntimeException{
        public TradeException(String message) {
            super(message);
        }
        public TradeException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    private static class Orcherstration {
        public static Orcherstration create(){
            return new Orcherstration();
        }

        public Orcherstration then(Runnable task){
            task.run();
            return this;
        }

        public <T> T then(Callable<T> task){
            try {
                return task.call();
            } catch (Exception e) {
                throw Throwables.propagate(e);
            }
        }
    }

    private static class Orchestrations{
        public static Runnable wait(final int magnitude, final TimeUnit timeUnit){
            return new Runnable() {
                @Override
                public void run() {
                    Uninterruptibles.sleepUninterruptibly(magnitude, timeUnit);
                }
            };
        }
    }

    private static final Logger LOGGER = Slf4jLoggerFactory.create();

    private Callable<String> getResult() {
        return new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "some result";
            }
        };
    }

    private static void someCodeThatThrowsTimeoutChecked() throws TimeoutCheckedException {
        throw new TimeoutCheckedException();
    }

    private static void letsCleanSomeResources(){
        //some code to clean some resources like connections, result sets, statements, streams.
    }

    private void shoutIfInvalide(String tradeId) {
        checkArgument(isNotBlank(tradeId));
    }

    private void shoutIfInvalide(Trader trader) {
        checkArgument(trader != null);
    }

    private void shoutIfInvalide(Operation operation) {
        checkArgument(operation != null);
    }

    private void processNormally(String tradeId) {
        LOGGER.info("processing normally [{}]", tradeId);
    }

    private ResultSet getTradeInfoFromDatabase(String tradeId) {
        return mock(ResultSet.class);
    }

    private void processNormally(ResultSet rs) {
        LOGGER.info("processing normally [{}]", rs);
    }

    private void processNormally(Trader rs) {
        LOGGER.info("processing normally [{}]", rs);
    }

    private void processNormally(Operation rs) {
        LOGGER.info("processing normally [{}]", rs);
    }

    private static class User {
        private final int age;

        private User(int age) {
            this.age = age;
        }

        private User(){
            this(Integer.MAX_VALUE);
        }
    }

    private static User userSuppliedHisAge(final int age){
        return new User(age);
    }

    private static User userDidNotSupplyHisAge(){
        return new User();
    }

    private static class Trader {}
    private static enum Operation {}

    private void rollBackAll(final Object ...params) {
        LOGGER.info("rolling back all what i have done for [{}]", params);
    }

    private static class RareException extends RuntimeException{}

    private static void handle(Exception e){
        LOGGER.error("handling exception by emailing stakeholders whenever an error happens", e);
    }

    private static <T>  void process(T t){
        LOGGER.info("processing [{}]", t);
    }

    private static class K {}

    private static Connection createConnection(){
        return mock(Connection.class);
    }

    private static void ohMyGodProcessing(){
        //some oh my god processing here
    }

    private static boolean isNecessary(){
        return randomBoolean();
    }

    private static void sendEmailToOperations(){
        LOGGER.info("sending email to operations with regard to some errors");
    }

    private static void logErrorMessageAppropriately(final Exception e){
        LOGGER.error("", e);
    }
}