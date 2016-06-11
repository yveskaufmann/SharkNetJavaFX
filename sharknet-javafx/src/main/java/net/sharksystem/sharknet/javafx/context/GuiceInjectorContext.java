package net.sharksystem.sharknet.javafx.context;

import com.google.inject.Binding;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import javafx.application.Application;
import net.sharksystem.sharknet.javafx.utils.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Supplier;

public class GuiceInjectorContext implements DIContext {

	private static final Logger Log = LoggerFactory.getLogger(GuiceInjectorContext.class);

	private Object rootObject;
	private Supplier<Collection<Module>> moduleSupplier;
	private Injector injector;

	public GuiceInjectorContext(Object rootObject, Supplier<Collection<Module>> moduleSupplier) {
		this.moduleSupplier = Objects.requireNonNull(moduleSupplier);
		this.rootObject = Objects.requireNonNull(rootObject);
	}

	@Override
	public void init() {
		injector = Guice.createInjector(moduleSupplier.get());
		injector.injectMembers(rootObject);
		moduleSupplier = null;
		Log.info("Created DI context");
	}

	@Override
	public void injectMembers(Object object) {
		injector.injectMembers(object);
	}

	@Override
	public <T> T getInstance(Class<T> type) {
		return injector.getInstance(type);
	}

	@Override
	public void destroy() {
		Log.info("Destroy DI context");
		injector.getAllBindings().values().stream()
			.map((binding -> binding.getProvider().get()))
			.forEach((obj) -> {
				Log.info("Destroying " + obj.getClass().getSimpleName());
				try {
					ReflectionUtils.invokeMethodsWithAnnotation(obj.getClass(), obj, PreDestroy.class);
				} catch (Exception ex) {
					Log.warn("Failed to destroy instance of " + obj.getClass().getSimpleName());
				}
			});
	}

}
