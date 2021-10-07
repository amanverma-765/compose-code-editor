package com.github.n34t0.ipw.impl;

import com.github.n34t0.ipw.impl.edt.EdtUtil;
import com.github.n34t0.ipw.impl.services.impl.ApplicationServiceImpl;
import com.github.n34t0.ipw.impl.services.impl.FileServiceImpl;
import com.github.n34t0.ipw.IpwProject;
import com.github.n34t0.ipw.IntellijPlatformWrapper;
import com.github.n34t0.ipw.impl.services.ApplicationService;
import com.github.n34t0.ipw.impl.services.FileService;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.util.Disposer;

import java.nio.file.Files;
import java.nio.file.Path;

public class Wrapper implements IntellijPlatformWrapper {
    private final Disposable rootDisposable;
    private FileService fileService;
    private ApplicationService applicationService;
    private static boolean isInitialized;
    private static boolean wasInitialized;

    public Wrapper() {
        rootDisposable = Disposer.newDisposable("IpwRootDisposable");
    }

    @Override
    public void init() {
        if (wasInitialized) return;
        setSystemSettings();
        fileService = new FileServiceImpl();
        fileService.init(rootDisposable);

        applicationService = new ApplicationServiceImpl();
        applicationService.init(fileService);

        wasInitialized = true;
        isInitialized = true;
    }

    @Override
    public void stop() {
        if (!isInitialized) return;
        EdtUtil.runInEdtAndWait(() -> Disposer.dispose(rootDisposable));
        Disposer.assertIsEmpty();
        isInitialized = false;
    }

    @Override
    public IpwProject openProject(String rootFolder) {
        checkIsInit();
        var path = Path.of(rootFolder);
        if (!Files.isDirectory(path)) {
            throw new IllegalArgumentException("Path " + rootFolder + " does not exist or is not a directory");
        }
        return new Project(path, rootDisposable, applicationService, fileService);
    }

    @Override
    public IpwProject openFile(String filePath) {
        checkIsInit();
        var path = Path.of(filePath);
        if (!Files.isRegularFile(path)) {
            throw new IllegalArgumentException("File " + filePath + " does not exist or is not a file");
        }
        return new Project(path, rootDisposable, applicationService, fileService);
    }

    private void checkIsInit() {
        if (!wasInitialized) {
            throw new IllegalStateException("the wrapper is not initialized");
        }
    }

    private void setSystemSettings() {
        System.setProperty("java.awt.headless", "true");
        System.setProperty("idea.platform.prefix", "Idea");
        System.setProperty("idea.ignore.disabled.plugins", "true");
        System.setProperty("jna.nosys", "true");

        // command line Java applications need a way to launch without a Dock icon.
        System.setProperty("apple.awt.UIElement", "true");

        // running disposer in debug mode
        System.setProperty("idea.disposer.debug", Boolean.getBoolean("ipw.debug") ? "on" : "off");

        // disable storing stack traces when PSI elements are invalidated
        System.setProperty("psi.track.invalidation", "false");

        System.setProperty("idea.use.native.fs.for.win", "false");
    }

}
