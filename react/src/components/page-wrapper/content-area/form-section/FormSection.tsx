import TextField from "./text-field/TextField";
import SubmissionHistory from "./submission-history/SubmissionHistory";

// Sample data - you can replace this with real data later
const sampleSubmissions = [
  {
    id: "1",
    name: "contract.pdf",
    size: "850 KB",
    type: "pdf" as const,
    createdAt: new Date(2026, 1, 3, 14, 30),
  },
  {
    id: "2",
    name: "Email.txt",
    size: "45 KB",
    type: "txt" as const,
    createdAt: new Date(2026, 1, 3, 14, 35),
  },
  {
    id: "3",
    name: "Lorem ipsum, bla bla...",
    size: "850 characters",
    type: "plain" as const,
    createdAt: new Date(2026, 1, 3, 15, 45),
  },
  {
    id: "4",
    name: "report.pdf",
    size: "1.2 MB",
    type: "pdf" as const,
    createdAt: new Date(2026, 1, 3, 16, 50),
  },
];

function FormSection() {
  return (
    <div className="bg-white w-[50%] h-full rounded-md shadow-lg shadow-black/15 ring-1 ring-black/5 p-4 flex flex-col gap-4">
      <div className="flex-1 overflow-y-auto">
        <SubmissionHistory submissions={sampleSubmissions} />
      </div>
      <TextField />
    </div>
  );
}

export default FormSection;
