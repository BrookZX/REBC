package odata.rebc.model;
 
public class rebcEntityActionResult {
//	private Entity entity;
	private boolean created = false;

 

//	public Entity getEntity() {
//		return entity;
//	}
//
//	public rebcEntityActionResult setEntity(final Entity entity) {
//		this.entity = entity;
//		return this;
//	}

	public boolean isCreated() {
		return created;
	}

	public rebcEntityActionResult setCreated(final boolean created) {
		this.created = created;
		return this;
	}

}
