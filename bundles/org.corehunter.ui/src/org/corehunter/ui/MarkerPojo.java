/*******************************************************************************
 * Copyright 2017 Guy Davenport
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

package org.corehunter.ui;
import java.util.LinkedList;
import java.util.List;

import uno.informatics.data.pojo.SimpleEntityPojo;

public class MarkerPojo extends SimpleEntityPojo implements Marker {

	private List<Allele> alleles ;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public MarkerPojo(String name) {
		super(name, name);
		
		alleles = new LinkedList<Allele>() ;
	}

	@Override
	public List<Allele> getAlleles() {
		return alleles ;
	}
	
	/**
	 * Add a new Allele to this marker 
	 * 
	 * @param alleleName the name of the allele
	 * @return the allele created
	 */
	public final Allele addAllele(String alleleName) {
		
		AllelePojo allele = new AllelePojo(alleleName, this) ;
		
		alleles.add(allele) ;
		
		return allele ;
	}
	
	private class AllelePojo extends SimpleEntityPojo implements Allele {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		private MarkerPojo marker ;
		
		public AllelePojo(String name, MarkerPojo marker) {
			super(name);
			
			this.marker = marker ;
		}

		@Override
		public Marker getMarker() {
			// TODO Auto-generated method stub
			return marker;
		}
	}
}
