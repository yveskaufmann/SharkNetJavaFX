package net.sharksystem.sharknet.javafx.modules;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.AbstractMatcher;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import net.sharksystem.sharknet.api.SharkNet;
import net.sharksystem.sharknet.javafx.services.ImageManager;
import net.sharksystem.sharknet.javafx.services.ReleaseManager;
import net.sharksystem.sharknet.javafx.services.SharkNetProvider;
import net.sharksystem.sharknet.javafx.utils.ReflectionUtils;
import net.sharksystem.sharknet.javafx.utils.controller.AbstractController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;

/***
 * This module contains all bindings for the SharkNet Api.
 *
 * For more information about guice injection modules
 * see: https://github.com/google/guice/wiki/GettingStarted.
 */
public class SharkNetModule extends AbstractModule {

	private Logger Log = LoggerFactory.getLogger(SharkNetModule.class);

	@Override
	protected void configure() {
		bind(ImageManager.class).asEagerSingleton();
		bind(SharkNet.class).toProvider(SharkNetProvider.class).asEagerSingleton();
		bind(ReleaseManager.class).asEagerSingleton();

		ensurePostConstructIsInvoked();
	}

	private void ensurePostConstructIsInvoked() {
		// ensures that PostConstruct is invoked after initialization.
		bindListener(new AbstractMatcher<TypeLiteral<?>>() {
			@Override
			public boolean matches(TypeLiteral<?> typeLiteral) {
				return true;
			}
		}, new TypeListener() {
			@Override
			public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
				encounter.register((InjectionListener<I>) injectee -> {
					if (! (injectee instanceof AbstractController)) {
						boolean wasInitialized = false;
						for (Method method : ReflectionUtils.getMethodsWithAnnotation(injectee.getClass(), PostConstruct.class)) {
							ReflectionUtils.invokeMethod(injectee, method);
							wasInitialized = true;
						}

						if (wasInitialized) {
							Log.info("Initialized " + injectee.getClass().getSimpleName());
						}
					}
				});
			}
		});
	}
}
