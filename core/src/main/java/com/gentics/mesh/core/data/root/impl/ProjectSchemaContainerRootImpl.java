package com.gentics.mesh.core.data.root.impl;

import static com.gentics.mesh.core.data.relationship.GraphRelationships.HAS_SCHEMA_ROOT;

import java.util.List;

import com.gentics.mesh.core.data.Project;
import com.gentics.mesh.core.data.Release;
import com.gentics.mesh.core.data.impl.ProjectImpl;
import com.gentics.mesh.core.data.schema.SchemaContainer;

/**
 * Project specific implementation of schema container root
 */
public class ProjectSchemaContainerRootImpl extends SchemaContainerRootImpl {
	/**
	 * Get the project
	 * @return project
	 */
	protected Project getProject() {
		return in(HAS_SCHEMA_ROOT).has(ProjectImpl.class).nextOrDefaultExplicit(ProjectImpl.class, null);
	}

	@Override
	public void addSchemaContainer(SchemaContainer schema) {
		super.addSchemaContainer(schema);

		// assign the latest schema version to all releases of the project
		List<? extends Release> releases = getProject().getReleaseRoot().findAll();
		for (Release release : releases) {
			release.assignSchemaVersion(schema.getLatestVersion());
		}
	}

	@Override
	public void removeSchemaContainer(SchemaContainer schemaContainer) {
		super.removeSchemaContainer(schemaContainer);

		// unassign the schema from all releases
		List<? extends Release> releases = getProject().getReleaseRoot().findAll();
		for (Release release : releases) {
			release.unassignSchema(schemaContainer);
		}
	}
}
