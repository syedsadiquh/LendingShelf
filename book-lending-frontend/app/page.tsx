"use client"

import { useState } from "react"
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { LibraryBig } from "lucide-react"
import UsersPanel from "@/features/users/users-panel"
import BooksPanel from "@/features/books/books-panel"
import BorrowingsPanel from "@/features/borrowings/borrowings-panel"
import BaseUrlSettings from "@/components/base-url"
import { Separator } from "@/components/ui/separator"

export default function Page() {
  const [tab, setTab] = useState("users")

  return (
    <main className="container mx-auto max-w-6xl px-4 py-8">
      <header className="mb-6 flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
        <div className="flex items-center gap-3">
          <LibraryBig className="h-8 w-8 text-foreground" aria-hidden="true" />
          <div>
            <h1 className="text-2xl font-bold">Book Lending System</h1>
            <p className="text-sm text-muted-foreground">Manage users, books, and borrowings</p>
          </div>
        </div>
        <BaseUrlSettings />
      </header>

      <Card className="mb-6">
        <CardHeader>
          <CardTitle>Workspace</CardTitle>
          <CardDescription>Use the tabs to view and manage Users, Books, and Borrowings.</CardDescription>
        </CardHeader>
        <CardContent>
          <Tabs value={tab} onValueChange={setTab} className="w-full">
            <TabsList className="flex w-full flex-wrap">
              <TabsTrigger value="users" className="flex-1">
                Users
              </TabsTrigger>
              <TabsTrigger value="books" className="flex-1">
                Books
              </TabsTrigger>
              <TabsTrigger value="borrowings" className="flex-1">
                Borrowings
              </TabsTrigger>
            </TabsList>
            <Separator className="my-4" />
            <TabsContent value="users" className="space-y-6">
              <UsersPanel />
            </TabsContent>
            <TabsContent value="books" className="space-y-6">
              <BooksPanel />
            </TabsContent>
            <TabsContent value="borrowings" className="space-y-6">
              <BorrowingsPanel />
            </TabsContent>
          </Tabs>
        </CardContent>
      </Card>
    </main>
  )
}
