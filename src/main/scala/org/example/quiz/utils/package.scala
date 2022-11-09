package org.example.quiz

import cats.effect.IO

package object utils {
  implicit class Debugger[A](io: IO[A]) {
    def debug: IO[A] = io.map(a => {
      println(s"[${Thread.currentThread().getName}] $a")
      a
    })
  }
}
