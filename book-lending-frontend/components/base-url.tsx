"use client"

import { useEffect, useState } from "react"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover"
import { Settings2 } from "lucide-react"
import { useToast } from "@/hooks/use-toast";

export default function BaseUrlSettings() {
  const [open, setOpen] = useState(false)
  const [value, setValue] = useState("http://localhost:8080")
  const { toast } = useToast()

  useEffect(() => {
    const saved = window.localStorage.getItem("apiBaseUrl")
    if (saved && saved.trim()) setValue(saved.trim())
  }, [])

  function save() {
    try {
      new URL(value)
    } catch {
      toast({ title: "Invalid URL", description: "Please enter a valid API base URL.", variant: "destructive" })
      return
    }
    window.localStorage.setItem("apiBaseUrl", value)
    toast({ title: "Saved", description: "API base URL updated." })
    setOpen(false)
  }

  return (
    // <Popover open={open} onOpenChange={setOpen}>
    //   <PopoverTrigger asChild>
    //     <Button variant="outline" className="gap-2 bg-transparent">
    //       <Settings2 className="h-4 w-4" />
    //       API Settings
    //     </Button>
    //   </PopoverTrigger>
    //   <PopoverContent className="w-80">
    //     <div className="grid gap-2">
    //       <Label htmlFor="api-base-url">API Base URL</Label>
    //       <Input
    //         id="api-base-url"
    //         placeholder="http://localhost:8080"
    //         value={value}
    //         onChange={(e) => setValue(e.target.value)}
    //       />
    //       <p className="text-xs text-muted-foreground">Your Java Spring Boot server base URL.</p>
    //       <div className="flex justify-end gap-2 pt-2">
    //         <Button variant="ghost" onClick={() => setOpen(false)}>
    //           Cancel
    //         </Button>
    //         <Button onClick={save}>Save</Button>
    //       </div>
    //     </div>
    //   </PopoverContent>
    // </Popover>
    null
  )
}
