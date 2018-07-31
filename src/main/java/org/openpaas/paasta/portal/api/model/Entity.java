
package org.openpaas.paasta.portal.api.model;

import java.util.Date;
import java.util.Map;
import java.util.UUID;


public class Entity {

	public Map<String, Object> map;
	private Meta meta;
	private String name;

	public Entity() {
		//empty
	}

	public Entity(Meta meta) {
		this(meta, null);
	}

	public Entity(Meta meta, String name) {
		if (meta != null) {
			this.meta = meta;
		}
		else {
			this.meta = Meta.defaultMeta();
		}
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Meta getMeta() {
		return meta;
	}

	public void setMeta(Meta meta) {
		this.meta = meta;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + ": (" +
				(meta == null || meta.getGuid() == null ? "-" : meta.getGuid()) + ") " +
				getName();
	}

	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}

	public static class Meta {

		private UUID guid;
		private Date created;
		private Date updated;
		private String url;

		public Meta(){
		}

		public Meta(UUID guid, Date created, Date updated) {
			this.guid = guid;
			this.created = created;
			this.updated = updated;
		}

		public Meta(UUID guid, Date created, Date updated, String url) {
			this.guid = guid;
			this.created = created;
			this.updated = updated;
			this.url = url;
		}

		public static Meta defaultMeta() {
			return new Meta(null, null, null);
		}

		public UUID getGuid() {
			return guid;
		}

		public Date getCreated() {
			return created;
		}

		public Date getUpdated() {
			return updated;
		}

		public String getUrl() {
			return url;
		}
	}
}
