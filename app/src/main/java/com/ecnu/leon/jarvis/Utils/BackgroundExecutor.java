package com.ecnu.leon.jarvis.Utils;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import android.os.Looper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public final class BackgroundExecutor {
    private static final int THREAD_NUMBER = 5;
    private static final String TAG = "BackgroundExecutor";
    private static Map<BackgroundExecutor.ThreadType, Executor> executorMap;
    public static final BackgroundExecutor.WrongThreadListener DEFAULT_WRONG_THREAD_LISTENER;
    private static BackgroundExecutor.WrongThreadListener wrongThreadListener;
    private static final List<BackgroundExecutor.Task> TASKS;
    private static final ThreadLocal<String> CURRENT_SERIAL;

    private static void init() {
        if (executorMap == null) {
            executorMap = new HashMap();
            ScheduledExecutorService ioExecutor = Executors.newScheduledThreadPool(5);
            ScheduledExecutorService networkExecutor = Executors.newScheduledThreadPool(5);
            ScheduledExecutorService calExecutor = Executors.newScheduledThreadPool(5);
            executorMap.put(BackgroundExecutor.ThreadType.IO, ioExecutor);
            executorMap.put(BackgroundExecutor.ThreadType.NETWORK, networkExecutor);
            executorMap.put(BackgroundExecutor.ThreadType.CALCULATION, calExecutor);
        }

    }

    private BackgroundExecutor() {
    }

    private static Future<?> directExecute(Runnable runnable, long delay, BackgroundExecutor.ThreadType threadType) {
        Object future = null;
        Executor executor = (Executor) executorMap.get(threadType);
        if (delay > 0L) {
            if (!(executor instanceof ScheduledExecutorService)) {
                throw new IllegalArgumentException("The executor set does not support scheduling");
            }

            ScheduledExecutorService executorService = (ScheduledExecutorService) executor;
            future = executorService.schedule(runnable, delay, TimeUnit.MILLISECONDS);
        } else if (executor instanceof ExecutorService) {
            ExecutorService executorService1 = (ExecutorService) executor;
            future = executorService1.submit(runnable);
        } else {
            executor.execute(runnable);
        }

        return (Future) future;
    }

    public static synchronized void execute(BackgroundExecutor.Task task) {
        Future future = null;
        if (task.serial == null || !hasSerialRunning(task.serial)) {
            task.executionAsked = true;
            future = directExecute(task, task.remainingDelay, task.threadType);
        }

        if (task.id != null || task.serial != null) {
            task.future = future;
            TASKS.add(task);
        }

    }

    public static void execute(final Runnable runnable, final String id, final long delay, final String serial, final BackgroundExecutor.ThreadType threadType) {
        execute(new BackgroundExecutor.Task(id, delay, serial, threadType) {
            public void execute() {
                try {
                    runnable.run();
                } catch (Throwable var2) {
                    Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), var2);
                }

            }
        });
    }

    public static void execute(Runnable runnable, long delay, BackgroundExecutor.ThreadType threadType) {
        directExecute(runnable, delay, threadType);
    }

    public static void execute(Runnable runnable, BackgroundExecutor.ThreadType threadType) {
        directExecute(runnable, 0L, threadType);
    }

    public static void execute(Runnable runnable, String id, String serial, BackgroundExecutor.ThreadType threadType) {
        execute(runnable, id, 0L, serial, threadType);
    }

    public static void setExecutor(Executor executor, BackgroundExecutor.ThreadType threadType) {
        executorMap.put(threadType, executor);
    }

    public static void setWrongThreadListener(BackgroundExecutor.WrongThreadListener listener) {
        wrongThreadListener = listener;
    }

    public static synchronized void cancelAll(String id, boolean mayInterruptIfRunning) {
        for (int i = TASKS.size() - 1; i >= 0; --i) {
            BackgroundExecutor.Task task = (BackgroundExecutor.Task) TASKS.get(i);
            if (id.equals(task.id)) {
                if (task.future != null) {
                    task.future.cancel(mayInterruptIfRunning);
                    if (!task.managed.getAndSet(true)) {
                        task.postExecute();
                    }
                } else if (task.executionAsked) {
                    Log.w("BackgroundExecutor", "A task with id " + task.id + " cannot be cancelled (the executor set does not support it)");
                } else {
                    TASKS.remove(i);
                }
            }
        }

    }

    public static void checkUiThread() {
        if (Looper.getMainLooper().getThread() != Thread.currentThread()) {
            wrongThreadListener.onUiExpected();
        }

    }

    public static void checkBgThread(String... serials) {
        if (serials.length == 0) {
            if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
                wrongThreadListener.onBgExpected(serials);
            }

        } else {
            String current = (String) CURRENT_SERIAL.get();
            if (current == null) {
                wrongThreadListener.onWrongBgSerial((String) null, serials);
            } else {
                String[] var2 = serials;
                int var3 = serials.length;

                for (int var4 = 0; var4 < var3; ++var4) {
                    String serial = var2[var4];
                    if (serial.equals(current)) {
                        return;
                    }
                }

                wrongThreadListener.onWrongBgSerial(current, serials);
            }
        }
    }

    private static boolean hasSerialRunning(String serial) {
        Iterator var1 = TASKS.iterator();

        BackgroundExecutor.Task task;
        do {
            if (!var1.hasNext()) {
                return false;
            }

            task = (BackgroundExecutor.Task) var1.next();
        } while (!task.executionAsked || !serial.equals(task.serial));

        return true;
    }

    private static BackgroundExecutor.Task take(String serial) {
        int len = TASKS.size();

        for (int i = 0; i < len; ++i) {
            if (serial.equals(((BackgroundExecutor.Task) TASKS.get(i)).serial)) {
                return (BackgroundExecutor.Task) TASKS.remove(i);
            }
        }

        return null;
    }

    static {
        init();
        DEFAULT_WRONG_THREAD_LISTENER = new BackgroundExecutor.WrongThreadListener() {
            public void onUiExpected() {
                throw new IllegalStateException("Method invocation is expected from the UI thread");
            }

            public void onBgExpected(String... expectedSerials) {
                if (expectedSerials.length == 0) {
                    throw new IllegalStateException("Method invocation is expected from a background thread, but it was called from the UI thread");
                } else {
                    throw new IllegalStateException("Method invocation is expected from one of serials " + Arrays.toString(expectedSerials) + ", but it was called from the UI thread");
                }
            }

            public void onWrongBgSerial(String currentSerial, String... expectedSerials) {
                if (currentSerial == null) {
                    currentSerial = "anonymous";
                }

                throw new IllegalStateException("Method invocation is expected from one of serials " + Arrays.toString(expectedSerials) + ", but it was called from " + currentSerial + " serial");
            }
        };
        wrongThreadListener = DEFAULT_WRONG_THREAD_LISTENER;
        TASKS = new ArrayList();
        CURRENT_SERIAL = new ThreadLocal();
    }

    public interface WrongThreadListener {
        void onUiExpected();

        void onBgExpected(String... var1);

        void onWrongBgSerial(String var1, String... var2);
    }

    public abstract static class Task implements Runnable {
        private String id;
        private long remainingDelay;
        private long targetTimeMillis;
        private String serial;
        private boolean executionAsked;
        private Future<?> future;
        private BackgroundExecutor.ThreadType threadType;
        private AtomicBoolean managed = new AtomicBoolean();

        public Task(String id, long delay, String serial, BackgroundExecutor.ThreadType threadType) {
            if (!"".equals(id)) {
                this.id = id;
            }

            if (delay > 0L) {
                this.remainingDelay = delay;
                this.targetTimeMillis = System.currentTimeMillis() + delay;
            }

            if (!"".equals(serial)) {
                this.serial = serial;
            }

            this.threadType = threadType;
        }

        public void run() {
            if (!this.managed.getAndSet(true)) {
                try {
                    BackgroundExecutor.CURRENT_SERIAL.set(this.serial);
                    this.execute();
                } finally {
                    this.postExecute();
                }

            }
        }

        public abstract void execute();

        private void postExecute() {
            if (this.id != null || this.serial != null) {
                BackgroundExecutor.CURRENT_SERIAL.set((String) null);
                Class var1 = BackgroundExecutor.class;
                synchronized (BackgroundExecutor.class) {
                    BackgroundExecutor.TASKS.remove(this);
                    if (this.serial != null) {
                        BackgroundExecutor.Task next = BackgroundExecutor.take(this.serial);
                        if (next != null) {
                            if (next.remainingDelay != 0L) {
                                next.remainingDelay = Math.max(0L, next.targetTimeMillis - System.currentTimeMillis());
                            }

                            BackgroundExecutor.execute(next);
                        }
                    }

                }
            }
        }
    }

    public static enum ThreadType {
        IO,
        NETWORK,
        CALCULATION;

        private ThreadType() {
        }
    }
}
