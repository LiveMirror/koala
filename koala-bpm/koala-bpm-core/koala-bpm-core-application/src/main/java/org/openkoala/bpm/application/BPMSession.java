package org.openkoala.bpm.application;

import org.drools.KnowledgeBase;
import org.drools.runtime.StatefulKnowledgeSession;

public interface BPMSession {

	public KnowledgeBase getKnowledgeBase();
	
	public StatefulKnowledgeSession getStatefulKnowledgeSession();
}
