//package com.javaclasses;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

class Pair{
	public String name;
	public String type;
	public Object value;
	public ArrayList<Pair> inner;
	public String object_name;
};

class Wrapper{
		private static final Set<Class<?>> WRAPPER_TYPES = getWrapperTypes();
	    public static boolean isWrapperType(Class<?> clazz){
	        return WRAPPER_TYPES.contains(clazz);
	    }
	    public static boolean isWrapperType(Object o){
	        return WRAPPER_TYPES.contains(o.getClass());
	    }
	    private static Set<Class<?>> getWrapperTypes(){
	        Set<Class<?>> ret = new HashSet<Class<?>>();
	        
	        ret.add(Boolean.class);
	        ret.add(Character.class);
	        ret.add(Byte.class);
	        ret.add(Short.class);
	        ret.add(Integer.class);
	        ret.add(Long.class);
	        ret.add(Float.class);
	        ret.add(Double.class);
	        ret.add(Void.class);
	        ret.add(String.class);
	        return ret;
	    }
};

class ObjectToDOM implements ObjectToDOMInterface{
	Document doc = null;
	Element classState = null;
	Element rootElement = null;
	
	public ArrayList<Pair> analizeClass(Object o, String s){
        ArrayList<Pair> fieldPairs = new ArrayList<>();
		try {
			Class<?> class_reflected = o.getClass();
			Pair mp = new Pair();
			
			if (s == null)
				mp.type = "Object";
			else {
				mp.type = "Object";
				mp.object_name = s;
			}
			
			mp.name = class_reflected.getName();
			fieldPairs.add(mp);
	
			  for (Class<?> c = class_reflected; c != null; c = c.getSuperclass()) {
			        Field[] fields = c.getDeclaredFields();
			        
			        for (Field classField : fields){			      
			        	if ( Modifier.isPublic(classField.getModifiers()) && Wrapper.isWrapperType(classField.get(o)) ){
				        	Pair p = new Pair();
				        	p.name = classField.getName();
				        	p.type = classField.getType().toString();
				        	p.value = classField.get(o);
				    	 
				        	fieldPairs.add(p);   	
			        	}else if (Modifier.isPublic(classField.getModifiers()) && !Wrapper.isWrapperType(classField.get(o))){
			        		Pair p = new Pair();
			        		p.inner = this.analizeClass(classField.get(o), classField.getName());
			        		
			        		fieldPairs.add(p);
			        	}
			        }
			    }
		} catch (Exception exception){
			exception.printStackTrace();
		}
		
		return fieldPairs;
	}
	
	private Element makeClass(ArrayList<Pair> array){
		Element _e = doc.createElement(array.get(0).object_name);
        Attr attrType = doc.createAttribute("type");
        attrType.setValue("Object");
        _e.setAttributeNode(attrType);
        int i = 0;
        
        Element _classState = this.doc.createElement("classState");
        
		for (Pair p: array){
			if (p.inner == null && i == 0){
				  Element className = this.doc.createElement("className");
			      className.appendChild(this.doc.createTextNode(p.name));
		          _e.appendChild( className );
			}else if (p.inner == null && i > 0){
				Element f = doc.createElement(p.name);
		        Attr _attrType = doc.createAttribute("type");
		        _attrType.setValue(p.type);
		        f.setAttributeNode(_attrType);
		        f.appendChild( doc.createTextNode( p.value.toString() ) );
		        _classState.appendChild(f);
			}else if (p.inner != null){
				_classState.appendChild( this.makeClass(p.inner) );
			}
			i++;
		}
		_e.appendChild(_classState);
		
		return _e;
	}
	
	
	private Document makeDoc(ArrayList<Pair> array) throws ParserConfigurationException{
	
	     DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	     DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	     this.doc = dBuilder.newDocument();

	     this.rootElement = doc.createElement("Object");
         this.doc.appendChild(this.rootElement);
         
			for (Pair p: array){
				if (p.inner == null){ //for mainclass
				  if (p.type == "Object" && p.object_name == null){ 
				        Element className = this.doc.createElement("className");
				        className.appendChild(this.doc.createTextNode(p.name));
					    this.rootElement.appendChild(className);
					    
					    this.classState = this.doc.createElement("classState");
				         
				  }else if (p.type != "Object" && p.object_name == null){
					   	Element _e = doc.createElement(p.name);
				        Attr attrType = doc.createAttribute("type");
				        attrType.setValue(p.type);
				        _e.setAttributeNode(attrType);
				        _e.appendChild( doc.createTextNode( p.value.toString() ) );
				        this.classState.appendChild(_e);
				  }
				}else{ //for subclass
					this.classState.appendChild( this.makeClass(p.inner) );
				}
				  
			  }
			 this.rootElement.appendChild(this.classState);

		return doc;
	}
	
	@Override
	public Document getDocument(Object o) {
		try{
			return this.makeDoc( this.analizeClass(o, null) );
		}catch (Exception exception){
			exception.printStackTrace();
		}return null;
	}
};
