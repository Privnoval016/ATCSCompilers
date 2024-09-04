	# This is a MIPS converted version of PASCAL source code that has been run
	# through a compiler in Java.
	# @author Pranav Sukesh
	# @date   2024-01-08
	.data
	newline: .asciiz "\n"
	varx: .word 0
	vary: .word 0
	varz: .word 0
	varw: .word 0
	varignore: .word 0
	.text
	.globl main
	main: #QTSPIM will automatically look for main
	li $v0 2
	la $t0 varx
	sw $v0 ($t0)    # store global variable x
	lw $v0 varx   # load global variable x
	subu $sp $sp 4 #pushes $v0 onto the stack
	sw $v0 ($sp)
	li $v0 1
	lw $t0 ($sp) #pops the top of the stack into $t0
	addu $sp $sp 4
	addu $v0 $t0 $v0 #adds the values and stores in $v0
	la $t0 vary
	sw $v0 ($t0)    # store global variable y
	lw $v0 varx   # load global variable x
	subu $sp $sp 4 #pushes $v0 onto the stack
	sw $v0 ($sp)
	lw $v0 vary   # load global variable y
	lw $t0 ($sp) #pops the top of the stack into $t0
	addu $sp $sp 4
	addu $v0 $t0 $v0 #adds the values and stores in $v0
	la $t0 varx
	sw $v0 ($t0)    # store global variable x
	lw $v0 varx   # load global variable x
	subu $sp $sp 4 #pushes $v0 onto the stack
	sw $v0 ($sp)
	lw $v0 vary   # load global variable y
	lw $t0 ($sp) #pops the top of the stack into $t0
	addu $sp $sp 4
	mult $v0 $t0 #multiplies the values and stores in $v0
	mflo $v0
	move $a0 $v0
	li $v0 1
	syscall
	la $a0 newline
	li $v0 4
	syscall #printed something
	lw $v0 varx   # load global variable x
	subu $sp $sp 4 #pushes $v0 onto the stack
	sw $v0 ($sp)
	lw $v0 vary   # load global variable y
	lw $t0 ($sp) #pops the top of the stack into $t0
	addu $sp $sp 4
	ble $t0 $v0 endif0
	lw $v0 varx   # load global variable x
	move $a0 $v0
	li $v0 1
	syscall
	la $a0 newline
	li $v0 4
	syscall #printed something
	lw $v0 vary   # load global variable y
	move $a0 $v0
	li $v0 1
	syscall
	la $a0 newline
	li $v0 4
	syscall #printed something
	j endif1
endif0:
endif1:
	li $v0 0
	la $t0 varx
	sw $v0 ($t0)    # store global variable x
while2:
	lw $v0 varx   # load global variable x
	subu $sp $sp 4 #pushes $v0 onto the stack
	sw $v0 ($sp)
	li $v0 10
	lw $t0 ($sp) #pops the top of the stack into $t0
	addu $sp $sp 4
	bge $t0 $v0 endwhile3
	lw $v0 varx   # load global variable x
	move $a0 $v0
	li $v0 1
	syscall
	la $a0 newline
	li $v0 4
	syscall #printed something
	lw $v0 varx   # load global variable x
	subu $sp $sp 4 #pushes $v0 onto the stack
	sw $v0 ($sp)
	li $v0 1
	lw $t0 ($sp) #pops the top of the stack into $t0
	addu $sp $sp 4
	addu $v0 $t0 $v0 #adds the values and stores in $v0
	la $t0 varx
	sw $v0 ($t0)    # store global variable x
	j while2
endwhile3:
	lw $v0 vary   # load global variable y
	move $a0 $v0
	li $v0 1
	syscall
	la $a0 newline
	li $v0 4
	syscall #printed something
	li $v0 4
	subu $sp $sp 4 #pushes $v0 onto the stack
	sw $v0 ($sp)
	lw $v0 varx   # load global variable x
	subu $sp $sp 4 #pushes $v0 onto the stack
	sw $v0 ($sp)
	lw $v0 vary   # load global variable y
	subu $sp $sp 4 #pushes $v0 onto the stack
	sw $v0 ($sp)
	jal procAdd    # Procedure call
	lw $t0 ($sp) #pops the top of the stack into $t0
	addu $sp $sp 4
	lw $t0 ($sp) #pops the top of the stack into $t0
	addu $sp $sp 4
	lw $t0 ($sp) #pops the top of the stack into $t0
	addu $sp $sp 4
	la $t0 varignore
	sw $v0 ($t0)    # store global variable ignore
	lw $v0 varx   # load global variable x
	move $a0 $v0
	li $v0 1
	syscall
	la $a0 newline
	li $v0 4
	syscall #printed something
	li $v0 10
	syscall #halt
	#procedures
procAdd:
	subu $sp $sp 4 #pushes $zero onto the stack
	sw $zero ($sp)
	subu $sp $sp 4 #pushes $ra onto the stack
	sw $ra ($sp)
	lw $v0 16($sp)   # load local variable y
	move $a0 $v0
	li $v0 1
	syscall
	la $a0 newline
	li $v0 4
	syscall #printed something
	lw $v0 12($sp)   # load local variable w
	move $a0 $v0
	li $v0 1
	syscall
	la $a0 newline
	li $v0 4
	syscall #printed something
	lw $v0 8($sp)   # load local variable z
	move $a0 $v0
	li $v0 1
	syscall
	la $a0 newline
	li $v0 4
	syscall #printed something
	lw $v0 varx   # load global variable x
	subu $sp $sp 4 #pushes $v0 onto the stack
	sw $v0 ($sp)
	lw $v0 20($sp)   # load local variable y
	lw $t0 ($sp) #pops the top of the stack into $t0
	addu $sp $sp 4
	addu $v0 $t0 $v0 #adds the values and stores in $v0
	la $t0 varx
	sw $v0 ($t0)    # store global variable x
	lw $ra ($sp) #pops the top of the stack into $ra
	addu $sp $sp 4
	lw $v0 0($sp)   #load return value
	jr $ra  #return to caller
