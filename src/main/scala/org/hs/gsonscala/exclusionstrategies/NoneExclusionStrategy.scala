package org.hs.gsonscala.exclusionstrategies

import com.google.gson

/**
  * Created by serhakan on 4/11/16.
  */
class NoneExclusionStrategy extends gson.ExclusionStrategy {
  def shouldSkipClass(clazz:Class[_]) = clazz == None.getClass

  def shouldSkipField(f: gson.FieldAttributes) = f.getDeclaredClass() == None.getClass

}
