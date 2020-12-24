package com.armadillo.game.model;

public enum MaskBits {

  PHYSICS_ENTITY(0x1),  // 0001
  WORLD_ENTITY(0x1 << 1); // 0010 or 0x2 in hex

  public final short mask;
  MaskBits(int mask) {
     this.mask = (short)mask;
  }

}
