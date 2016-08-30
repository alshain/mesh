package com.gentics.mesh.dagger;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Central singleton which provides the dagger mesh dependency context.
 */
public interface MeshCore {

	static AtomicReference<MeshComponent> applicationComponent = new AtomicReference<>(null);

	static MeshComponent create() {

		if (applicationComponent.get() == null) {
			applicationComponent.set(DaggerMeshComponent.builder().build());
		}
		return applicationComponent.get();

	}

	static <T extends MeshComponent> T get() {
		return (T) applicationComponent.get();
	}

}
